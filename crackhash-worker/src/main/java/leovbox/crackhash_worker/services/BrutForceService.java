package leovbox.crackhash_worker.services;

import leovbox.crackhash_worker.models.TaskStatus;
import leovbox.crackhash_worker.requests.TaskRequest;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.paukov.combinatorics.*;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.paukov.combinatorics.CombinatoricsFactory.createPermutationWithRepetitionGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

@Service
/// Сервис для перебора перестановок и нахождения значения хэш кода
public class BrutForceService {

    private final ConcurrentHashMap<String, TaskStatus> tasks = new ConcurrentHashMap<>();

    public static String calculateMD5(List<Character> input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Преобразуем List<Character> в массив байтов
            byte[] bytes = new byte[input.size()];
            for (int i = 0; i < input.size(); i++) {
                bytes[i] = (byte) input.get(i).charValue();
            }

            byte[] digest = md.digest(bytes);

            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    static public List<String> BrutForce(TaskRequest request) {
        List<String> result = new ArrayList<>();

        // Создаем генератор перестановок.
        ICombinatoricsVector<Character> vector = createVector(request.getAlphabet());
        for (int i = 1; i <= request.getMaxLength(); i++) {
            Generator<Character> gen = createPermutationWithRepetitionGenerator(vector, i);

            long iSymbolWordsCount = gen.getNumberOfGeneratedObjects();
            long batchVolume = iSymbolWordsCount / request.getPartCount();
            long startIdx = batchVolume * request.getPartNumber();
            long endIdx = startIdx + batchVolume;

            Iterator<ICombinatoricsVector<Character>> iterator = gen.iterator();
            for (long j = 0; j < startIdx && iterator.hasNext(); j++) {
                iterator.next(); // Пропускаем ненужные элементы
            }

            for (long j = startIdx; j < endIdx && iterator.hasNext(); j++) {
                ICombinatoricsVector<Character> combination = iterator.next();
                List<Character> word = combination.getVector();

                if (calculateMD5(word).equals(request.getHash())) {
                    result.add(convertToString(word));
                    System.out.println("result added " + convertToString(word));
                }
            }
        }
        System.out.println("brutForce " + request.getTaskId() + " ended");

        return result;
    }

    public TaskStatus getTaskStatus(String taskId) {
        return tasks.get(taskId);
    }

    public static String convertToString(List<Character> charList) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : charList) {
            sb.append(ch);
        }
        return sb.toString();
    }

    public CompletableFuture<Void> startBrutForceAsync(TaskRequest request, TaskStatus taskStatus) {
        // Создаем новую задачу и добавляем её в ConcurrentHashMap
//        TaskStatus taskStatus = new TaskStatus("IN_PROGRESS", new ArrayList<>());
//        tasks.put(request.getTaskId(), taskStatus);

        // Запускаем асинхронное выполнение
        return CompletableFuture.runAsync(() -> {
            try {
                List<String> result = BrutForce(request);
                taskStatus.setStatus("COMPLETED");
                taskStatus.setResult(result);
            } catch (Exception e) {
                taskStatus.setStatus("FAILED");
            } finally {
                tasks.put(request.getTaskId(), taskStatus);
            }
        });
    }
}
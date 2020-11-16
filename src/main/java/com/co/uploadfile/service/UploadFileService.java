package com.co.uploadfile.service;

import com.co.uploadfile.properties.FileUploadProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadFileService {

    private final FileUploadProperties fileUploadProperties;


    public void copyFileOnDirectory(MultipartFile multipartFile, Long id) throws IOException {
        if (!multipartFile.isEmpty()) {
            Path root = getPath(fileUploadProperties.getLocation());
            Files.copy(multipartFile.getInputStream(),
                    root.resolve(String.valueOf(id).concat(multipartFile.getOriginalFilename())));
            readFile(String.valueOf(id).concat(multipartFile.getOriginalFilename()),id);
        }
    }

    private Path getPath(String location) throws IOException {
        Path root = Paths.get(location).toAbsolutePath().normalize();
        if (!Files.exists(root)) {
            Files.createDirectory(root);
        }
        return root;
    }

    private void readFile(String name, Long id) throws IOException {
        Stream<String> readFile = Files.lines(Path.of(fileUploadProperties.getLocation().concat("/" + name)));
        Stream<String> splitFile = readFile.flatMap(line -> Stream.of(line.split("[\\r\\n]+")));
        List<String> listOfNumbers = splitFile.collect(Collectors.toList());
        log.debug("list of numbers", listOfNumbers);
        processFile(listOfNumbers,id);
    }

    private void processFile(List<String> numbers, Long id) throws IOException {
        List<Integer> newList = numbers.stream()
                .map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        String result = elementByDay(newList);
        getPath(fileUploadProperties.getOutput());
        Path filePath = Paths.get(fileUploadProperties.getOutput(),
                String.valueOf(id).concat(fileUploadProperties.getName()));
        writeFile(filePath, result);
    }

    private static void writeFile(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }

    private String elementByDay(List<Integer> newList) {
        Map<Integer, List<Integer>> workDays = new HashMap<>();
        Integer count = 0;
        String result = "";
        Integer day = newList.get(0);
        Integer positionNextDay = newList.indexOf(newList.get(1));
        while (day > 0) {
            count++;
            Integer nextPosition = positionNextDay + 1;
            positionNextDay = newList.get(positionNextDay) + nextPosition;
            workDays.put(day, listOfWeights(nextPosition, positionNextDay, newList));
            Integer trips = tripsMade(workDays.get(day));
            String tripsPerDay = String.format("Case # %1$s : %2$s", count, trips);
            result = String.format("%1$s %2$s \n", result, tripsPerDay);
            log.debug("trips per day", tripsPerDay);
            day--;
        }
        return result;
    }

    private List<Integer> listOfWeights(Integer nextPosition, Integer sizeElement, List<Integer> numbers) {
        List<Integer> weights = new ArrayList<>();
        while (nextPosition < sizeElement) {
            weights.add(numbers.get(nextPosition));
            nextPosition++;
        }
        return weights;
    }

    private Integer tripsMade(List<Integer> weights) {
        Integer weightRead = Integer.parseInt(fileUploadProperties.getWeight());
        Integer trips = 0;
        Integer weight = 0;
        if (verifyAllEqual(weights) && weights.stream().reduce(0, Integer::sum) < weightRead) {
            return 1;
        }
        while (weight < weightRead) {
            if (weights.isEmpty()) {
                break;
            }
            OptionalInt maxNumber = weights.stream().mapToInt(value -> value).max();
            OptionalInt minNumber = weights.stream().mapToInt(value -> value).min();
            weight = maxNumber.getAsInt() + minNumber.getAsInt();
            if (weight < weightRead) {
                weights.remove((Integer) maxNumber.getAsInt());
                weights.remove((Integer) minNumber.getAsInt());
            } else {
                weight = maxNumber.getAsInt();
                weights.remove((Integer) maxNumber.getAsInt());
            }
            trips++;
        }
        return trips;
    }

    private boolean verifyAllEqual(List<Integer> weights) {
        return weights.stream().distinct().count() <= 1;
    }
}

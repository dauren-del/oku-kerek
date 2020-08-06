package kz.oku.kerek.service.impl;

import kz.oku.kerek.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmazonS3StorageService implements FileStorageService {

    private final S3Client amazonS3Client;
    private final S3Presigner amazonS3Presigner;
    private String bucketName = "kazneb";

    @Override
    public void putFile(Path filePath, String fileName) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        amazonS3Client.putObject(putObjectRequest, RequestBody.fromFile(filePath));
    }

    @Override
    public byte[] getFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return amazonS3Client.getObjectAsBytes(getObjectRequest).asByteArray();
    }

    @Override
    public List<String> list(String directory) {

        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(directory)
                .build();

        ListObjectsResponse response = amazonS3Client.listObjects(listObjectsRequest);
        List<S3Object> s3Objects = response.contents();

        return s3Objects.stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    @Override
    public URL generateAccessUrl(String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(15L))
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest =
                amazonS3Presigner.presignGetObject(getObjectPresignRequest);

        return presignedGetObjectRequest.url();
    }

    @Override
    public boolean exists(String key) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(key)
                .build();

        ListObjectsResponse response = amazonS3Client.listObjects(listObjectsRequest);
        return !CollectionUtils.isEmpty(response.contents());
    }

    @Override
    public void deleteByExtensions(String directory, String extension) {
        List<String> objects = list(directory);
        List<ObjectIdentifier> toDelete = objects.stream()
                .filter(key -> key.endsWith("." + extension))
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .collect(Collectors.toList());

        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder().objects(toDelete).build())
                .build();

        amazonS3Client.deleteObjects(deleteObjectsRequest);
    }

}

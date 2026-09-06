package com.Forensics.CCIMS.Service;

import com.Forensics.CCIMS.Client.CaseClient;
import com.Forensics.CCIMS.DTO.EvidenceDownloadDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import com.Forensics.CCIMS.DTO.EvidenceRequestDTO;
import com.Forensics.CCIMS.DTO.EvidenceResponseDTO;
import com.Forensics.CCIMS.Entity.Evidence;
import com.Forensics.CCIMS.Exception.ResourceNotFoundException;
import com.Forensics.CCIMS.Repository.EvidenceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EvidenceService {

    @Autowired
    private EvidenceRepository evidenceRepository;

    @Autowired
    private CaseClient caseClient;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public EvidenceResponseDTO createEvidence(EvidenceRequestDTO request) {

        caseClient.getCaseById(request.getCaseId());

        Evidence evidence =
                modelMapper.map(request, Evidence.class);

        String username =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        evidence.setUploadedBy(username);
        evidence.setUploadedAt(LocalDateTime.now());

        Evidence savedEvidence =
                evidenceRepository.save(evidence);

        return modelMapper.map(
                savedEvidence,
                EvidenceResponseDTO.class
        );
    }

    public EvidenceResponseDTO getEvidenceById(String id) {

        Evidence evidence =
                evidenceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Evidence not found"
                                ));

        return modelMapper.map(
                evidence,
                EvidenceResponseDTO.class
        );
    }

    public List<EvidenceResponseDTO> getEvidenceByCaseId(String caseId) {

        return evidenceRepository.findByCaseId(caseId)
                .stream()
                .map(evidence ->
                        modelMapper.map(
                                evidence,
                                EvidenceResponseDTO.class
                        ))
                .toList();
    }

    public List<EvidenceResponseDTO> getEvidenceByUploader(String username) {

        return evidenceRepository.findByUploadedBy(username)
                .stream()
                .map(evidence ->
                        modelMapper.map(
                                evidence,
                                EvidenceResponseDTO.class
                        ))
                .toList();
    }

    public EvidenceResponseDTO deleteEvidence(String id) {

        Evidence evidence =
                evidenceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Evidence not found"
                                ));

        try {

            Path filePath =
                    Paths.get(evidence.getFilePath());

            Files.deleteIfExists(filePath);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete evidence file",
                    e
            );
        }

        evidenceRepository.delete(evidence);

        return modelMapper.map(
                evidence,
                EvidenceResponseDTO.class
        );
    }

    public String storeFile(MultipartFile file) {

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();

            String extension = "";

            if (originalFileName != null &&
                    originalFileName.contains(".")) {

                extension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf(".")
                        );
            }

            String storedFileName =
                    UUID.randomUUID() + extension;

            Path filePath =
                    uploadPath.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath
            );

            return filePath.toString();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to store evidence file",
                    e
            );
        }
    }

    public EvidenceResponseDTO uploadEvidence(
            EvidenceRequestDTO request,
            MultipartFile file) {

        String contentType = file.getContentType();



            if (contentType == null ||
                    !Set.of(
                            "application/pdf",
                            "image/jpeg",
                            "image/png",
                            "application/msword",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                            "video/mp4",
                            "video/x-msvideo",
                            "video/quicktime",
                            "audio/mpeg",
                            "audio/wav"
                    ).contains(contentType)) {

                throw new IllegalArgumentException(
                        "Unsupported evidence file type"
                );
            }

            if (file.isEmpty()) {
                throw new IllegalArgumentException(
                        "Evidence file cannot be empty"
                );
            }

            long maxFileSize;

            if (contentType.startsWith("video/")) {
                maxFileSize = 500L * 1024 * 1024; // 500 MB
            } else {
                maxFileSize = 10L * 1024 * 1024; // 10 MB
            }

            if (file.getSize() > maxFileSize) {
                throw new IllegalArgumentException(
                        "Evidence file exceeds the maximum allowed size"
                );
            }

            // Verify that the case exists
            caseClient.getCaseById(request.getCaseId());

            String filePath = storeFile(file);

        try {

            String username =
                    SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getName();

            Evidence evidence =
                    modelMapper.map(request, Evidence.class);

            String originalFileName = file.getOriginalFilename();

            String safeFileName = originalFileName != null
                    ? Paths.get(originalFileName).getFileName().toString()
                    : "unknown";

            evidence.setFileName(safeFileName);
            evidence.setFileType(file.getContentType());
            evidence.setFileSize(file.getSize());
            evidence.setFilePath(filePath);
            evidence.setUploadedBy(username);
            evidence.setUploadedAt(LocalDateTime.now());

            Evidence savedEvidence =
                    evidenceRepository.save(evidence);

            return modelMapper.map(
                    savedEvidence,
                    EvidenceResponseDTO.class
            );
        }

        catch (Exception e) {

            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException cleanupException) {
                cleanupException.printStackTrace();
            }

            throw new RuntimeException(
                    "Failed to save evidence",
                    e
            );
        }
    }

    public EvidenceDownloadDTO downloadEvidence(String id) {

        Evidence evidence =
                evidenceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Evidence not found"
                                ));

        try {

            Path filePath =
                    Paths.get(evidence.getFilePath());

            Resource resource =
                    new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException(
                        "Evidence file not found"
                );
            }

            return new EvidenceDownloadDTO(
                    resource,
                    evidence.getFileName(),
                    evidence.getFileType()
            );

        } catch (MalformedURLException e) {

            throw new RuntimeException(
                    "Could not read evidence file",
                    e
            );
        }
    }
}
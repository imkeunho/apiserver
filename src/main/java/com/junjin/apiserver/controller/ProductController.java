package com.junjin.apiserver.controller;

import com.junjin.apiserver.dto.PageRequestDTO;
import com.junjin.apiserver.dto.PageResponseDTO;
import com.junjin.apiserver.dto.ProductDTO;
import com.junjin.apiserver.service.ProductService;
import com.junjin.apiserver.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    private final ProductService productService;

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        log.info(uploadFileNames);

        long pno = productService.register(productDTO);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Map.of("RESULT", pno);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {

        return fileUtil.getFile(fileName);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return productService.getList(pageRequestDTO);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) {

        productDTO.setPno(pno);

        //old product Database saved product
        ProductDTO oldProductDTO = productService.get(pno);

        //file upload
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //keep file String
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {

            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO);

        //파일 삭제
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        if (oldFileNames != null && !oldFileNames.isEmpty()) {

            List<String> removeFiles = oldFileNames.stream()
                    .filter(fileName -> !uploadedFileNames.contains(fileName))
                    .toList();

            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {

        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }
}

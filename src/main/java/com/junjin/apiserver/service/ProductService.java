package com.junjin.apiserver.service;

import com.junjin.apiserver.dto.PageRequestDTO;
import com.junjin.apiserver.dto.PageResponseDTO;
import com.junjin.apiserver.dto.ProductDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface ProductService {

    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    Long register(ProductDTO productDTO);

    ProductDTO get(Long pno);

    void modify(ProductDTO productDTO);

    void remove(Long pno);
}

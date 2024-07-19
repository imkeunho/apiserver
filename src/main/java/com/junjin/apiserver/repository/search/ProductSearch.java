package com.junjin.apiserver.repository.search;

import com.junjin.apiserver.dto.PageRequestDTO;
import com.junjin.apiserver.dto.PageResponseDTO;
import com.junjin.apiserver.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO);
}

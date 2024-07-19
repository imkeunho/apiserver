package com.junjin.apiserver.repository.search;

import com.junjin.apiserver.dto.PageRequestDTO;
import org.springframework.data.domain.Page;
import com.junjin.apiserver.domain.Todo;

public interface TodoSearch {

    Page<Todo> search1(PageRequestDTO pageRequestDTO);
}

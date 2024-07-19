package com.junjin.apiserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {

    private List<E> dtoList;

    private List<Integer> pageNumList;

    private PageRequestDTO pageRequestDTO;

    private Boolean prev, next;

    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {

        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.current = pageRequestDTO.getPage();
        this.totalCount = (int) totalCount;

        //끝 페이지 end
        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;

        int start = end - 9;

        //진짜 마지막
        int last = (int) (Math.ceil(this.totalCount /(double) pageRequestDTO.getSize()));

        this.totalPage = last;

        end = Math.min(end, last);

        this.prev = start > 1;

        this.next = this.totalCount > end * pageRequestDTO.getSize();

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        this.prevPage = prev ? start - 1 : 0;

        this.nextPage = next ? end + 1 : 0;
    }
}

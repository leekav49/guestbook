package com.leekav.guestbook.domain;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<DTO, EN> {
    //응답 목록을 저장할 List
    private List<DTO> dtoList;

    //전체 페이지 개수
    private int totalPage;

    //현재 페이지 번호
    private int page;

    //한 페이지에 출력되는 데이터 개수
    private int size;

    //페이지 번호 목록의 시작 번호와 종료 번호
    private int start, end;

    //이전과 다음 존재 여부
    private boolean prev, next;

    //페이지 번호 목록
    private List<Integer> pageList;

    //페이지 번호 목록을 만들어주는 메서드
    private void makePageList(Pageable pageable) {
        //현재 페이지 번호와 페이지당 데이터 개수 가져오기
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        //임시 종료 페이지 번호
        //페이지 번호를 10개 출력할 것이라서 10으로 나누고 곱함
        //페이지 번호 개수를 다르게 하고자 하면 숫자를 변경
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        //시작 페이지 번호
        start = tempEnd - 9;
        //이전 존재 여부
        prev = start > 1;
        //종료 페이지 번호
        end = totalPage > tempEnd ? tempEnd:totalPage;
        next = totalPage > tempEnd;
        //페이지 번호 목록 만들기
        pageList = IntStream.rangeClosed(start, end)
                .boxed().collect(Collectors.toList());


    }

    //Page 객체와 변환 함수를 넘겨받아서 dtoList를 만들어주는 메서드
    public PageResponseDTO(Page<EN> result, Function<EN, DTO> fn){
        //Page 객체를 순회하면서 fn 함수로 변환한 후 List 만들기
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        //페이지 번호 목록 만들기
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }
}

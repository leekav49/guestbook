package com.leekav.guestbook.service;

import com.leekav.guestbook.domain.GuestBookDTO;
import com.leekav.guestbook.domain.PageRequestDTO;
import com.leekav.guestbook.domain.PageResponseDTO;
import com.leekav.guestbook.entity.GuestBook;
import com.leekav.guestbook.entity.QGuestBook;
import com.leekav.guestbook.persistent.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Log4j2
public class GuestBookServiceImpl implements GuestBookService{
    private final GuestBookRepository guestBookRepository;

    //데이터 삽입을 위한 메서드
    @Override
    public Long register(GuestBookDTO dto) {
        log.info("데이터 삽입");
        log.info(dto);
        //Repository에서 사용하기 위해서 DTO를 Entity로 변환
        GuestBook entity = dtoToEntity(dto);
        //데이터 삽입
        GuestBook result = guestBookRepository.save(entity);
        //삽입한 후 리턴받은 데이터의 gno 리턴
        return result.getGno();
    }

    @Override
    public PageResponseDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        //페이지 단위 요청을 위한 Pageable 객체를 생성
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        //데이터베이스에서 조회
        //Page<GuestBook> result = guestBookRepository.findAll(pageable);
        BooleanBuilder booleanBuilder = getSearch(requestDTO);
        Page<GuestBook> result = guestBookRepository.findAll(booleanBuilder, pageable);

        //Entity 를 DTO 로 변환하기 위한 객체 생성
        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDTO(entity));

        //데이터 목록 생성
        return new PageResponseDTO<>(result, fn);
    }

    @Override
    public GuestBookDTO read(Long gno) {
        Optional<GuestBook> guestBook = guestBookRepository.findById(gno);
        return guestBook.isPresent()? entityToDTO(guestBook.get()):null;
    }

    //검색 조건을 만들어주는 메서드
    private BooleanBuilder getSearch(
            PageRequestDTO requestDTO) {
        //검색 항목 가져오기
        String type = requestDTO.getType();
        //검색어 가져오기
        String keyword = requestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestBook qGuestBook = QGuestBook.guestBook;

        //검색 조건이 없는 경우
        if (type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        //검색 조건이 있는 경우
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")) {
            conditionBuilder.or(
                    qGuestBook.title.contains(keyword));
        }

        if(type.contains("c")) {
            conditionBuilder.or(
                    qGuestBook.title.contains(keyword));
        }

        if(type.contains("w")) {
            conditionBuilder.or(
                    qGuestBook.title.contains(keyword));
        }

        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }
}

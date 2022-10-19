package com.leekav.guestbook;

import com.leekav.guestbook.entity.GuestBook;
import com.leekav.guestbook.entity.QGuestBook;
import com.leekav.guestbook.persistent.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private GuestBookRepository guestBookRepository;

    //데이터 삽입 테스트
    //@Test
    public void insertTest() {
        //300 개의 가상의 데이터 삽입
        IntStream.rangeClosed(1, 300).forEach(i -> {
            GuestBook guestBook = GuestBook.builder()
                    .title("Title..." + i)
                    .content("Content" + i)
                    .writer("user" + (i % 10))
                    .build();
            guestBookRepository.save(guestBook);
        });
    }

    //데이터 수정 테스트
    //@Test
    public void updateTest() {
        //데이터 1개 찾아오기
        Optional<GuestBook> result = guestBookRepository.findById(103L);
        //데이터가 존재하는 경우 수정
        if (result.isPresent()) {
            GuestBook guestBook = result.get();
            guestBook.changeTitle("제목 변경");
            guestBook.changeContent("내용 수정");

            guestBookRepository.save(guestBook);
        } else {
            System.out.println("데이터가 존재하지 않음.");
        }
    }

    //@Test
    public void deleteTest() {
        //데이터 1개 찾아오기
        Optional<GuestBook> result = guestBookRepository.findById(103L);
        //데이터가 존재하는 경우 삭제
        if (result.isPresent()) {
            guestBookRepository.delete(result.get());
        } else {
            System.out.println("데이터가 존재하지 않음.");
        }
    }

    //전체 데이터 가져오기 테스트
    //@Test
    public void selectAllTest() {
        List<GuestBook> list = guestBookRepository.findAll();
        for (GuestBook guestBook : list) {
            System.out.println(guestBook);
        }
    }

    //데이터 querydsl 을 이용한 조회
    //@Test
    public void testQuery1() {
        //gno의 내림차순 정렬 후 0페이지 10개를 가져오기 위한 Pageable 생성
        Pageable pageable = PageRequest.of(
                0, 10, Sort.by("gno")
                        .descending());
        //Entity에 동적 쿼리를 수행할 수 있는 도메인 클래스 찾아오기
        //Querydsl 설정을 해야 사용 가능
        //컬럼들을 속성으로 포함시켜 조건을 설정하는 것이 가능
        QGuestBook qGuestBook = QGuestBook.guestBook;
        //검색어 생성
        String keyword = "1";
        //검색을 적용하기 위한 Builder 객체 생성
        BooleanBuilder builder = new BooleanBuilder();

        //조건 표현식 생성
        BooleanExpression expression =
                qGuestBook.title.contains(keyword);
        //검색 객체에 표현식을 추가
        builder.and(expression);
        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);
        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });

    }

    @Test
    public void testQuery2() {
        //gno의 내림차순 정렬 후 0페이지 10개를 가져오기 위한 Pageable 생성
        Pageable pageable = PageRequest.of(
                0, 10, Sort.by("gno")
                        .descending());
        //Entity에 동적 쿼리를 수행할 수 있는 도메인 클래스 찾아오기
        //Querydsl 설정을 해야 사용 가능
        //컬럼들을 속성으로 포함시켜 조건을 설정하는 것이 가능
        QGuestBook qGuestBook = QGuestBook.guestBook;
        //검색어 생성
        String keyword = "1";
        //검색을 적용하기 위한 Builder 객체 생성
        BooleanBuilder builder = new BooleanBuilder();

        //조건 표현식 생성
        BooleanExpression ex1 =
                qGuestBook.title.contains(keyword);
        BooleanExpression ex2 =
                qGuestBook.content.contains(keyword);
        //2개의 표현식을 or로 연결
        BooleanExpression expression = ex1.or(ex2);
        //검색 객체에 표현식을 추가
        builder.and(expression);
        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);
        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }

}



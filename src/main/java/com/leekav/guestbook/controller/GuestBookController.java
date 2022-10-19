package com.leekav.guestbook.controller;

import com.leekav.guestbook.domain.GuestBookDTO;
import com.leekav.guestbook.domain.PageRequestDTO;
import com.leekav.guestbook.service.GuestBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GuestBookController {
    //서비스 객체 주입
    private final GuestBookService guestBookService;

    @GetMapping({"/"})
    public String main() {
        log.info("list..............");
        return "redirect:/guestbook/list";
    }

    @GetMapping({"/guestbook/list"})
    public void list(PageRequestDTO dto, Model model) {
        log.info("list..............");
        //서비스 메서드 호출
        //result의 dtoList에 DTO의 List가 있고
        //result의 pageList에 페이지 번호의 List가 존재
        model.addAttribute("result", guestBookService.getList(dto));
    }

    //등록 요청을 GET 방식으로 처리하는 메서드 - 등록 페이지로 이동
    @GetMapping("/guestbook/register")
    public void register() {
        log.info("register GET...");
    }

    //등록 요청을 POST 방식으로 처리하는 메서드 - 등록 수행
    @PostMapping("/guestbook/register")
    public String register(GuestBookDTO dto, RedirectAttributes redirectAttributes) {
        log.info("register POST...");
        //등록 요청 처리
        Long gno = guestBookService.register(dto);

        //데이터 저장
        redirectAttributes.addFlashAttribute("msg", gno + "등록");

        //목록 보기로 리다이렉트
        return "redirect:/guestbook/list";
    }

    //상세보기 요청 처리
    @GetMapping("/guestbook/read")
    //ModelAttribute는 매개변수를 결과 페이지에 넘겨줄 때 사용
    public void read(long gno, @ModelAttribute("requestDTO")
    PageRequestDTO requestDTO, Model model ){
        log.info("gno: " + gno);
        GuestBookDTO dto = guestBookService.read(gno);
        model.addAttribute("dto", dto);
    }

}

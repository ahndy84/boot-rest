package com.community.rest.controller;

import com.community.rest.domain.Board;
import com.community.rest.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RepositoryRestController
public class BoardRestController {

	private BoardRepository boardRepository;

	public BoardRestController(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	@GetMapping("/boards")
	public @ResponseBody Resources<Board> simpleBoard(@PageableDefault Pageable pageable) {
		Page<Board> boardList = boardRepository.findAll(pageable);
		/*
			현재 페이지 수, 총 게시판 수, 한 페이지의 게시판 수 등 페이징 처리에 관한 리소스를 만드는 PagedResources 객체를 생성하기 위해
			PagedResources 생성자의 파라미터로 사용되는 PageMetadata 객체를 생성했습니다.
		 */
		PageMetadata pageMetadata = new PageMetadata( pageable.getPageSize(), boardList.getNumber(), boardList.getTotalElements());
		PagedResources<Board> resources = new PagedResources<>(boardList.getContent(), pageMetadata);
		resources.add(linkTo(methodOn(BoardRestController.class).simpleBoard(pageable)).withSelfRel());

		return resources;
	}


}

package com.community.rest.controller;

import com.community.rest.domain.Board;
import com.community.rest.repository.BoardRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/boards")
public class BoardRestController {

	private BoardRepository boardRepository;

	public BoardRestController(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBoards(@PageableDefault Pageable pageable) {
		Page<Board> boards = boardRepository.findAll(pageable);

		/*
			현재 페이지 수, 총 게시판 수, 한 페이지의 게시판 수 등 페이징 처리에 관한 리소스를 만드는 PagedResources 객체를 생성하기 위해
			PagedResources 생성자의 파라미터로 사용되는 PageMetadata 객체를 생성했습니다.
		 */
		PageMetadata pageMetadata = new PageMetadata(pageable.getPageSize(), boards.getNumber(), boards.getTotalElements());
		PagedResources<Board> resources = new PagedResources<>(boards.getContent(), pageMetadata);
		resources.add(linkTo(methodOn(BoardRestController.class).getBoards(pageable)).withSelfRel());

		return ResponseEntity.ok(resources);
	}

	/**
	 * @brief 저장하기
	 * @param board
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> postBoard(@RequestBody Board board) {
		//valid 체크
		System.out.println("111111111111111111111111 " + board);
		board.setCreatedDateNow();
		boardRepository.save(board);
		return new ResponseEntity<>("{}", HttpStatus.CREATED);
	}

	/**
	 * @brief 수정하기
	 * @param idx
	 * @param board
	 * @return
	 */
	@PutMapping("/{idx}")
	public ResponseEntity<?> putBoard(@PathVariable("idx")Long idx, @RequestBody Board board) {
		//valid 체크
		System.out.println("---------------------idx : " +idx);
		Board persistBoard = boardRepository.getOne(idx) ;
		persistBoard.update(board);
		boardRepository.save(persistBoard);
		return new ResponseEntity<>("{}", HttpStatus.OK);
	}

	/**
	 * @brief 삭제하기
	 * @param idx
	 * @return
	 */
	@DeleteMapping("/{idx}")
	public ResponseEntity<?> deleteBoard(@PathVariable("idx")Long idx) {
		//valid 체크
		boardRepository.deleteById(idx);
		return new ResponseEntity<>("{}", HttpStatus.OK);
	}
}
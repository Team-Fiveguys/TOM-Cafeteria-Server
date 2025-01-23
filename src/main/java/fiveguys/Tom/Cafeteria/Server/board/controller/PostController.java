package fiveguys.Tom.Cafeteria.Server.board.controller;

import fiveguys.Tom.Cafeteria.Server.apiPayload.ApiResponse;
import fiveguys.Tom.Cafeteria.Server.board.dto.*;
import fiveguys.Tom.Cafeteria.Server.board.entity.BoardType;
import fiveguys.Tom.Cafeteria.Server.board.entity.OrderType;
import fiveguys.Tom.Cafeteria.Server.board.entity.Post;
import fiveguys.Tom.Cafeteria.Server.board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @Operation(summary = "게시글 생성 API")
    @PostMapping
    public ApiResponse<String> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        Post post = postService.createPost(postCreateDTO);
        return ApiResponse.onSuccess(post.getId() + "번 게시물이 생성되었습니다.");
    }

    // 특정 게시판의 전체 게시글 조회
    @Operation(summary = "메뉴 건의 게시글 리스트 조회 API", description = "메뉴건의 요청 게시글 리스트를 조회한다. orderType을 통해 정렬할 기준을 page를 통해 받을 페이지를 받아 응답한다")
    @GetMapping("/menu-request")
    public ApiResponse<PostPreviewListDTO> getAllMenuRequestPosts(@RequestParam(name = "cafeteriaId") Long cafeteriaId,
                                                                  @RequestParam(name = "page") int page,
                                                                  @RequestParam(name = "orderType") OrderType orderType) {
        PostPreviewListDTO postList;
        if(orderType.equals(OrderType.TIME)) {
            postList = postService.getPostPageOrderedByTime(BoardType.MENU_REQUEST, cafeteriaId, page);
        }
        else{
            postList = postService.getPostPageOrderedByLike(BoardType.MENU_REQUEST, cafeteriaId, page);
        }
        return ApiResponse.onSuccess(postList);
    }

    @Operation(summary = "공지 게시글 리스트 조회 API", description = "공지사항 게시글 리스트를 조회한다. page를 통해 최신순으로 정렬된 페이지 목록을 응답한다")
    @GetMapping("/notice")
    public ApiResponse<PostPreviewListDTO> getAllNoticePosts(@RequestParam(name = "cafeteriaId") Long cafeteriaId,
                                                                @RequestParam(name = "page") int page) {
        PostPreviewListDTO postList;
        postList = postService.getPostPageOrderedByTime(BoardType.NOTICE, cafeteriaId, page);
        return ApiResponse.onSuccess(postList);
    }

//    @Operation(summary = "신고 게시글 리스트 조회 API", description = "신고 게시글 리스트를 조회한다. page를 통해 최신순으로 정렬된 페이지 목록을 응답한다")
//    @GetMapping("/notice")
//    public ApiResponse<List<PostPreviewDTO>> getAllReportedPosts(@RequestParam(name = "cafeteriaId") Long cafeteriaId) {
//        List<PostPreviewDTO> postList = postService.getReportedPostList(cafeteriaId);
//        return ApiResponse.onSuccess(postList);
//    }



    //특정 게시글 조회
    @Operation(summary = "특정 게시글 조회 API")
    @GetMapping("/{id}")
    public ApiResponse<PostResponseDTO> getBoardById(@PathVariable(name = "id") Long id) {
        PostResponseDTO postResponseDTO = postService.getPostById(id);
        return ApiResponse.onSuccess(postResponseDTO);
    }

    // 게시글 수정
    @Operation(summary = "특정 게시글 수정 API", description = "경로 변수로 postId를 받아 게시물을 식별하여 제목과 내용을 변경한다.")
    @PutMapping("/{id}")
    public ApiResponse<String> updateBoard(@PathVariable(name = "id") Long postId, @RequestBody PostUpdateDTO postUpdateDTO) {
        Post post = postService.updatePost(postId, postUpdateDTO);
        return ApiResponse.onSuccess(post.getId() + "번 게시물이 수정 되었습니다.");
    }

    // 게시글 삭제
    @Operation(summary = "특정 게시글 삭제 API")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePost(@PathVariable(name = "id") Long postId) {
        postService.deletePost(postId);
        return ApiResponse.onSuccess(postId + "번 게시물이 삭제되었습니다."); // 성공적으로 삭제 처리됐음을 응답. 별도의 데이터 반환 필요 없음
    }

    //게시글 좋아요
    @Operation(summary = "게시글에 좋아요/취소 토글 API")
    @PostMapping("/{id}/like")
    public ApiResponse<String> toggleLike(@PathVariable(name = "id") Long id) {
        if (postService.toggleLike(id)){
            return ApiResponse.onSuccess(id + "번 게시물에 좋아요를 눌렀습니다.");
        }
        else{
            return ApiResponse.onSuccess(id + "번 게시물에 좋아요를 취소하였습니다.");
        }
    }

    @Operation(summary = "게시글 신고하기 API")
    @PostMapping("/{id}/report")
    public ApiResponse<String> reportPost(@PathVariable(name = "id") Long id, ReportCreateDTO reportCreateDTO) {
        postService.reportPost(id, reportCreateDTO.getReportType(), reportCreateDTO.getContent());
        return ApiResponse.onSuccess(id + "번 게시물을 신고 하였습니다.");
    }

}


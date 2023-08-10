package wanted.wantedpreonboardingbackend.domain.post.controller;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import wanted.wantedpreonboardingbackend.domain.member.controller.MemberController;
import wanted.wantedpreonboardingbackend.domain.post.dto.PostSaveDto;
import wanted.wantedpreonboardingbackend.domain.post.dto.PostSearchCondition;
import wanted.wantedpreonboardingbackend.domain.post.dto.PostUpdateDto;
import wanted.wantedpreonboardingbackend.domain.post.entity.Post;
import wanted.wantedpreonboardingbackend.domain.post.service.PostService;

import javax.validation.Valid;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final Logger logger = LoggerFactory.getLogger(MemberController.class);


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public ResponseEntity<String> save(@Valid @RequestBody PostSaveDto postSaveDto) throws Exception {
        try {
            Post post = postService.save(postSaveDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Post created with ID: " + post.getId());
        }catch (MethodArgumentNotValidException ex) {
            logger.warn("Validation failed: {}", ex.getMessage());
            throw ex;
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity search(
            @RequestParam(value = "sort", defaultValue = "createDate") String sort,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @ModelAttribute PostSearchCondition postSearchCondition) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort).ascending());
        return ResponseEntity.ok(postService.getPostList(pageable, postSearchCondition));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get/{postId}")
    public ResponseEntity getInfo(@PathVariable ("postId") Long postId) throws Exception {
        return ResponseEntity.ok(postService.getPostInfo(postId));
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("update/{postId}")
    public ResponseEntity update(@PathVariable("postId") Long postId,
                                 @RequestBody PostUpdateDto postUpdateDto) throws Exception {

        return ResponseEntity.ok(postService.update(postId, postUpdateDto));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("delete/{postId}")
    public ResponseEntity delete(@PathVariable("postId") Long postId) throws Exception {
        return ResponseEntity.ok(postService.delete(postId));
    }

}

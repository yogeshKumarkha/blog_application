package com.blog.service.impl;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.exception.BlogAPIException;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.CommentDto;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import com.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper=mapper;
    }
    @Override
    public CommentDto saveComment(Long postId, CommentDto commentDto) {
        Post post =postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("post not found with id: "+postId)
        );
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment newComment =commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post =postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("post not found with id: "+postId)
        );
           Comment comment  =commentRepository.findById(commentId).orElseThrow(
                ()-> new ResourceNotFoundException("post not found with id: "+commentId)
        );
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
         Post post=postRepository.findById(postId).orElseThrow(
                 ()-> new ResourceNotFoundException("Post Not found with id:"+postId)
         );
         Comment comment=commentRepository.findById(id).orElseThrow(
                 ()-> new ResourceNotFoundException("Comment not found id:"+id)
         );
         if (!comment.getPost().getId().equals(post.getId())){
             throw  new BlogAPIException("Comment does not belong to post");
         }
         comment.setName(commentDto.getName());
         comment.setEmail(commentDto.getEmail());
         comment.setBody(commentDto.getBody());
        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long id) {
        Post post=postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("post Not found:"+postId)
        );
        Comment comment=commentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Comment does not post :"+id)
        );
        if (!comment.getPost().getId().equals(post.getId())){
            throw  new BlogAPIException("Comment does not belong to post");
        }
        commentRepository.deleteById(id);

    }

    Comment mapToEntity(CommentDto commentDto){
        Comment comment=mapper.map(commentDto,Comment.class);
        //Comment comment= new Comment();
        //comment.setBody(commentDto.getBody());
        //comment.setEmail(commentDto.getEmail());
        //comment.setName(commentDto.getName());
        return comment;
    }
    CommentDto mapToDto(Comment comment){
        CommentDto dto=mapper.map(comment,CommentDto.class);
       // CommentDto dto= new CommentDto();
        //dto.setId(comment.getId());
        //dto.setBody(comment.getBody());
        //dto.setEmail(comment.getEmail());
        //dto.setName(comment.getName());
        return dto;
    }


}

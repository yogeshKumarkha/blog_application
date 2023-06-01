package com.blog.service.impl;

import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.PostDto;
import com.blog.payload.PostResponse;
import com.blog.repository.PostRepository;
import com.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
     private PostRepository postRepository;
     private ModelMapper mapper;
    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper=mapper;
    }
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post=mapToEntity(postDto);

        Post savedPost = postRepository.save(post);
        PostDto dto= mapToDto(savedPost);
        return dto;
    }
    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> content = postRepository.findAll(pageable);
        List<Post> posts = content.getContent();

        List<PostDto> dtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(dtos);
        postResponse.setPageNo(content.getNumber());
        postResponse.setPageSize(content.getSize());
        postResponse.setTotalElements(content.getTotalElements());
        postResponse.setTotalPages(content.getTotalPages());
        postResponse.setIsLast(content.isLast());
        return postResponse;
    }
    @Override
    public PostDto getPostById(long id){
        Post post=postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Post Not Found id:"+id)
        );
        PostDto postDto= mapToDto(post);
        return postDto;
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
                      Post post=postRepository.findById(id).orElseThrow(
                              ()-> new ResourceNotFoundException("Post Not Found id:"+id)
              );
                      post.setTitle(postDto.getTitle());
                      post.setContent(postDto.getContent());
                      post.setContent(postDto.getContent());
                      Post updatedPost= postRepository.save(post);
                   return mapToDto(updatedPost);
    }
    @Override
    public void deletePost(long id) {
        Post post=postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Post Not Found id:"+id)
        );
        postRepository.deleteById(id);
    }
    Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
        // Post post= new Post();
        //post.setTitle(postDto.getTitle());
        //post.setDescription(postDto.getDescription());
        //post.setContent(postDto.getContent());
        return post;
    }
   PostDto mapToDto(Post post){
       PostDto dto = mapper.map(post, PostDto.class);
       //PostDto dto=new PostDto();
       //dto.setId(post.getId());
       //dto.setTitle(post.getTitle());
       //dto.setDescription(post.getDescription());
       //dto.setContent(post.getContent());
       return dto;

   }
}

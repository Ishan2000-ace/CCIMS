package com.Forensics.CCIMS.Service;

import com.Forensics.CCIMS.DTO.UserRequestDTO;
import com.Forensics.CCIMS.DTO.UserResponseDTO;
import com.Forensics.CCIMS.Entity.Users;
import com.Forensics.CCIMS.Exception.ResourceNotFoundException;
import com.Forensics.CCIMS.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserResponseDTO createUser(UserRequestDTO user){
         Users Euser = modelMapper.map(user, Users.class);

         Users saveduser = userRepository.save(Euser);

         return modelMapper.map(saveduser, UserResponseDTO.class);
    }

    public UserResponseDTO getUser(String id){
        Users nuser = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));

        return modelMapper.map(nuser, UserResponseDTO.class);
    }

    public UserResponseDTO updateUser(String id , UserRequestDTO newuser){
        Users olduser = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));

        olduser.setUsername(newuser.getUsername());
        olduser.setRole(newuser.getRole());
        olduser.setPassword(newuser.getPassword());

        Users updated = userRepository.save(olduser);

        return modelMapper.map(updated, UserResponseDTO.class);
    }

    public UserResponseDTO deleteUser(String id){
        Users deluser = userRepository.findById(id).orElseThrow();
        userRepository.delete(deluser);
        return modelMapper.map(deluser, UserResponseDTO.class);
    }
}

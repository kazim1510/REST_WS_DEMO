package com.webapp.app.ws.ui.entrypoints;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.BeanUtils;

import com.webapp.app.ws.annotations.Secured;
import com.webapp.app.ws.service.UserService;
import com.webapp.app.ws.service.impl.UserServiceImpl;
import com.webapp.app.ws.shared.dto.UserDTO;
import com.webapp.app.ws.ui.model.request.CreateUserRequestModel;
import com.webapp.app.ws.ui.model.request.UpdateUserRequestModel;
import com.webapp.app.ws.ui.model.response.DeleteUserProfileResponceModel;
import com.webapp.app.ws.ui.model.response.RequestOperation;
import com.webapp.app.ws.ui.model.response.ResponseStatus;
import com.webapp.app.ws.ui.model.response.UserProfileRest;

@Path("/users")
public class UserEntryPoint {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest createUser(CreateUserRequestModel requestObject) {
        UserProfileRest returnModel = new UserProfileRest();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(requestObject, userDTO);

        UserService userService = new UserServiceImpl();
        UserDTO createdUserProfie = userService.createUser(userDTO);

        BeanUtils.copyProperties(userDTO, returnModel);

        return returnModel;
    }

    @Secured
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest getUserProfile(@PathParam("id") String id) {
        UserProfileRest returnValue = null;

        UserService userService = new UserServiceImpl();
        UserDTO userDTO = userService.getUser(id);
        returnValue = new UserProfileRest();
        BeanUtils.copyProperties(userDTO, returnValue);

        return returnValue;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UserProfileRest> getUsers(@DefaultValue("0") @QueryParam("start") int start, @DefaultValue("50") @QueryParam("limit") int limit) {
        List<UserProfileRest> returnValue = null;

        UserService userService = new UserServiceImpl();
        List<UserDTO> users = userService.getUsers(start, limit);

        returnValue = new ArrayList<>();
        for (UserDTO userDTO : users) {
            UserProfileRest userModel = new UserProfileRest();
            BeanUtils.copyProperties(userDTO, userModel);
            userModel.setHref("users/" + userDTO.getUserId());
            returnValue.add(userModel);
        }

        return returnValue;
    }

    @Secured
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserProfileRest updateUserDetails(@PathParam("id") String id, UpdateUserRequestModel userDetails) {
        UserProfileRest returnValue = null;
        UserService userService = new UserServiceImpl();
        UserDTO userDTO = userService.getUser(id);

        if (userDetails.getFirstName() != null && !userDetails.getFirstName().isEmpty()) {
            userDTO.setFirstName(userDetails.getFirstName());
        }
        userDTO.setLastName(userDetails.getLastName());
        userService.updateUser(userDTO);
        returnValue = new UserProfileRest();
        BeanUtils.copyProperties(userDTO, returnValue);

        return returnValue;
    }

    @Secured
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeleteUserProfileResponceModel deleteUserDetails(@PathParam("id") String id) {
        DeleteUserProfileResponceModel returnValue = new DeleteUserProfileResponceModel();
        returnValue.setRequestOperation(RequestOperation.DELETE);

        UserService userService = new UserServiceImpl();
        UserDTO userDTO = userService.getUser(id);

        userService.deleteUser(userDTO);
        returnValue.setResponceStatus(ResponseStatus.SUCCESS);

        return returnValue;

    }


}

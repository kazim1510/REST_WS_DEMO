package com.webapp.app.ws.ui.entrypoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.BeanUtils;

import com.webapp.app.ws.service.UserService;
import com.webapp.app.ws.service.impl.UserServiceImpl;
import com.webapp.app.ws.shared.dto.UserDTO;
import com.webapp.app.ws.ui.model.request.CreateUserRequestModel;
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


}

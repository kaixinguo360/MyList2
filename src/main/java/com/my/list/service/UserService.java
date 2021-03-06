package com.my.list.service;

import com.my.list.Constants;
import com.my.list.entity.User;
import com.my.list.exception.DataException;
import com.my.list.exception.ForbiddenException;
import com.my.list.exception.UnauthorizedException;
import com.my.list.mapper.OptionMapper;
import com.my.list.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final OptionMapper optionMapper;

    public UserService(UserMapper userMapper, OptionMapper optionMapper, UserContext.UserContextFactory userContextFactory) {
        this.userMapper = userMapper;
        this.optionMapper = optionMapper;
        this.userContextFactory = userContextFactory;
    }

    // ---- CRUD ---- //
    public void add(User user) {
        if (user == null) throw new DataException("Input user is null");
        if (user.getId() != null) throw new DataException("Id of input user has already set.");
        userMapper.insert(user);
    }
    public User get(Long id) {
        if (id == null) throw new DataException("Input id is null");
        return userMapper.selectByPrimaryKey(id);
    }
    public List<User> getAll() {
        return userMapper.selectAll();
    }
    public void update(User user) {
        if (user == null) throw new DataException("Input user is null");
        if (user.getId() == null) throw new DataException("Id of input user is not set.");
        userMapper.updateByPrimaryKey(user);
        invalidateToken(user.getId());
    }
    public void remove(Long id) {
        if (id == null) throw new DataException("Input id is null");
        userMapper.deleteByPrimaryKey(id);
        invalidateToken(id);
    }

    // ---- Auth ---- //
    public User getByNameAndPass(String name, String pass) {
        if (name == null) throw new DataException("Input name is null");
        if (pass == null) throw new DataException("Input password is null");
        return userMapper.selectByNameAndPass(name, pass);
    }

    // ---- Token ---- //
    private final Map<String, UserContext> tokens = new HashMap<>();
    private final Map<Long, UserContext> userContexts = new HashMap<>();
    private final UserContext.UserContextFactory userContextFactory;

    public String generateToken(String name, String pass) {
        if (name == null) throw new DataException("Input name is null");
        if (pass == null) throw new DataException("Input password is null");
        // check password
        User user = getByNameAndPass(name, pass);
        if (user == null) throw new ForbiddenException("Wrong user name or password, name=" + name + ", pass=" + pass);
        // create token and user context
        String token = UUID.randomUUID().toString();
        if (!userContexts.containsKey(user.getId())) userContexts.put(user.getId(), userContextFactory.create(user));
        UserContext userContext = userContexts.get(user.getId());
        // add to tokens map, and return token
        tokens.put(token, userContext);
        return token;
    }
    public void invalidateToken(String token) {
        if (token == null) throw new DataException("Input token is null");
        if (!tokens.containsKey(token)) throw new UnauthorizedException("No such token, token=" + token);
        UserContext userContext = tokens.remove(token);
        if (!tokens.containsValue(userContext)) userContexts.remove(userContext.user.getId());
    }
    public void invalidateToken(Long userId) {
        userContexts.remove(userId);
        tokens.entrySet().removeIf(entry -> userId.equals(entry.getValue().user.getId()));
    }
    public UserContext getUserContext(String token) {
        if (token == null) throw new DataException("Input token is null");
        if (!tokens.containsKey(token)) throw new UnauthorizedException("No such token, token=" + token);
        return tokens.get(token);
    }
    
    // ---- Admin ---- //
    public Set<String> adminTokens = new HashSet<>();
    public String generateAdminToken(String pass) {
        if (pass == null) throw new DataException("Input admin password is null");
        // check password
        String savedPass = optionMapper.select("admin_pass");
        if (savedPass == null) {
            savedPass = Constants.DEFAULT_ADMIN_PASS;
            optionMapper.insert("admin_pass", savedPass);
        }
        if (!pass.equals(savedPass)) throw new ForbiddenException("Wrong admin password, pass=" + pass);
        // create token
        String token = UUID.randomUUID().toString();
        // add to tokens list, and return token
        adminTokens.add(token);
        return token;
    }
    public void invalidateAdminToken(String token) {
        if (token == null) throw new DataException("Input admin token is null");
        if (!adminTokens.contains(token)) throw new UnauthorizedException("No such token, token=" + token);
        adminTokens.remove(token);
    }
    public void checkAdminToken(String token) {
        if (token == null) throw new DataException("Input admin token is null");
        if (!adminTokens.contains(token)) throw new UnauthorizedException("No such admin token, token=" + token);
    }
    

}

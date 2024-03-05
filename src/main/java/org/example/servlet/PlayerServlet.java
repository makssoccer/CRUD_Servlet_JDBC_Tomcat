package org.example.servlet;

import org.example.configuration.DataSourceCofig;
import org.example.dto.PlayerDto;
import org.example.mapper.impl.PlayerConverterImpl;
import org.example.repository.impl.PlayerRepositoryImpl;
import org.example.service.PlayerService;
import org.example.service.impl.PlayerServiceImpl;
import org.example.util.JsonSender;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PlayerServlet", urlPatterns = {"/players/*"})
public class PlayerServlet extends HttpServlet {
    private final PlayerService playerService = new PlayerServiceImpl(new PlayerRepositoryImpl(DataSourceCofig.getDataSource()),new PlayerConverterImpl()); // Предположим, что у вас есть реализация CardService
    private static String pathInfo;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            getAllPlayers(request, response);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                int id = Integer.parseInt(splits[1]);
                getPlayerById(request, response, id);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PlayerDto playerDto = getPlayerDtoFromRequest(request);
        PlayerDto createdPlayerDto = playerService.create(playerDto);
        JsonSender.sendAsJson(response, createdPlayerDto);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(splits[1]);
        PlayerDto playerDto = getPlayerDtoFromRequest(request);
        playerDto.setId(id);
        PlayerDto updatedPlayerDto = playerService.update(playerDto);
        JsonSender.sendAsJson(response, updatedPlayerDto);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(splits[1]);
        playerService.delete(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getAllPlayers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<PlayerDto> playerDtos = playerService.readAll();
        JsonSender.sendAsJson(response, playerDtos);
    }

    private void getPlayerById(HttpServletRequest request, HttpServletResponse response, int id) throws IOException {
        PlayerDto cardDto = playerService.read(id);
        if (cardDto != null) {
            JsonSender.sendAsJson(response, cardDto);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private PlayerDto getPlayerDtoFromRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        return JsonSender.gson.fromJson(reader, PlayerDto.class);
    }

}
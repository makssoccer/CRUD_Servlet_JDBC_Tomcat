package org.example.servlet;

import org.example.configuration.DataSourceCofig;
import org.example.dto.CardDto;
import org.example.mapper.impl.CardConverterImpl;
import org.example.repository.impl.CardRepositoryImpl;
import org.example.service.CardService;
import org.example.service.impl.CardServiceImpl;
import org.example.util.JsonSender;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CardServlet", urlPatterns = {"/cards/*"})
public class CardServlet extends HttpServlet {
    private final CardService cardService = new CardServiceImpl(new CardRepositoryImpl(DataSourceCofig.getDataSource()), new CardConverterImpl()); // Предположим, что у вас есть реализация CardService
    private static String pathInfo;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            getAllCards(response);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                int id = Integer.parseInt(splits[1]);
                getCardById(response, id);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CardDto cardDto = getCardDtoFromRequest(request);
        CardDto createdCardDto = cardService.create(cardDto);
        JsonSender.sendAsJson(response, createdCardDto);
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
        CardDto cardDto = getCardDtoFromRequest(request);
        cardDto.setId(id);
        CardDto updatedCardDto = cardService.update(cardDto);
        JsonSender.sendAsJson(response, updatedCardDto);
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
        cardService.delete(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getAllCards(HttpServletResponse response) throws IOException {
        List<CardDto> cardDtos = cardService.readAll();
        JsonSender.sendAsJson(response, cardDtos);
    }

    private void getCardById(HttpServletResponse response, int id) throws IOException {
        CardDto cardDto = cardService.read(id);
        if (cardDto != null) {
            JsonSender.sendAsJson(response, cardDto);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private CardDto getCardDtoFromRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        return JsonSender.gson.fromJson(reader, CardDto.class);
    }
}
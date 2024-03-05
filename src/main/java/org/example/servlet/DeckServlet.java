package org.example.servlet;

import org.example.configuration.DataSourceCofig;
import org.example.dto.DeckDto;
import org.example.mapper.impl.DeckConverterImpl;
import org.example.repository.impl.DeckRepositoryImpl;
import org.example.service.DeckService;
import org.example.service.impl.DeckServiceImpl;
import org.example.util.JsonSender;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DeckServlet", urlPatterns = {"/decks/", "/decks/*"})
public class DeckServlet extends HttpServlet {
    private final DeckService deckService = new DeckServiceImpl(new DeckRepositoryImpl(DataSourceCofig.getDataSource()), new DeckConverterImpl()); // Предположим, что у вас есть реализация CardService
    private static String pathInfo;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            getAllDecks(request, response);
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                int id = Integer.parseInt(splits[1]);
                getDeckById(request, response, id);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            DeckDto deckDto = getDeckDtoFromRequest(request);
            DeckDto createdDeckDto = deckService.create(deckDto);
            JsonSender.sendAsJson(response, createdDeckDto);
        } else {
            pathInfo = pathInfo.substring(1);
            Integer deckId = Integer.parseInt(pathInfo);
            DeckDto deckDto = getDeckDtoFromRequest(request);
            deckDto.setId(deckId);
            DeckDto createdDeckDto = deckService.create(deckDto);
            JsonSender.sendAsJson(response, createdDeckDto);
        }
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
        DeckDto deckDto = getDeckDtoFromRequest(request);
        deckDto.setId(id);
        DeckDto updatedDeckDto = deckService.update(deckDto);
        JsonSender.sendAsJson(response, updatedDeckDto);
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
        deckService.delete(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getAllDecks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<DeckDto> deckDtos = deckService.readAll();
        JsonSender.sendAsJson(response, deckDtos);
    }

    private void getDeckById(HttpServletRequest request, HttpServletResponse response, int id) throws IOException {
        DeckDto cardDto = deckService.read(id);
        if (cardDto != null) {
            JsonSender.sendAsJson(response, cardDto);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private DeckDto getDeckDtoFromRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        return JsonSender.gson.fromJson(reader, DeckDto.class);
    }

}
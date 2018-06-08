package com.playground.ewnclient.server;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerResponse {

    public ServerResponseCode code;
    public String message;
    private Pattern pattern;

    public ServerResponse(String message) {
        pattern = Pattern.compile("^Server\\s([A-Z][0-9]{0,3})>\\s(.*)$");
        this.message = message;
        this.parseMessage();
    }

    private void parseMessage() {
        Matcher matcher = pattern.matcher(this.message);
        while (matcher.find()) {
            this.code = ServerResponseCode.valueOf(matcher.group(1));
            this.message = matcher.group(2);
        }
    }

    public List<String> availableOpponents() {
        // Example response: "Server B> Folgende Spieler waeren bereit zu spielen: cb  cb1  cb2"
        if (!message.contains("Folgende Spieler waeren bereit zu spielen")) {
            return null;
        }
        List<String> availablePlayers = new ArrayList<String>();
        Pattern pattern = Pattern.compile(".*:\\s(.*)$");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            availablePlayers = Arrays.asList(matcher.group(1).split("\\s+"));
        }
        return availablePlayers;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}: {1}", code, message);
    }
}

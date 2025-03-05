package de.fhdw.Kino.Lib.command;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.List;

public class CommandResponseDeserializer extends StdDeserializer<CommandResponse> {

    public CommandResponseDeserializer() {
        this(CommandResponse.class);
    }

    protected CommandResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CommandResponse deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        ObjectMapper objectMapper = (ObjectMapper) p.getCodec();

        CommandResponse.CommandStatus status = objectMapper.convertValue(
                node.get("status"),
                CommandResponse.CommandStatus.class
        );

        String message = objectMapper.convertValue(
                node.get("message"),
                String.class
        );

        CommandResponse.ResponseEntityType responseEntityType = objectMapper.convertValue(
                node.get("responseEntityType"),
                CommandResponse.ResponseEntityType.class
        );

        Object entity = null;
        if (node.has("entity")) {
            entity =  switch (responseEntityType) {
                case RESERVIERUNG, KINO, KUNDE, FILM, AUFFUEHRUNG -> objectMapper.convertValue(node.get("entity"), responseEntityType.DTOclass);
                case RESERVIERUNGSLISTE, KUNDENLISTE, FILMLISTE, AUFFUEHRUNGSLISTE -> objectMapper.convertValue(node.get("entity"), objectMapper.getTypeFactory().constructCollectionType(List.class, responseEntityType.DTOclass));
                case EMPTY -> null;
            };
        }

        return new CommandResponse(status, message, responseEntityType, entity);
    }
}
package de.fhdw.Kino.Lib.command;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CommandRequestDeserializer extends StdDeserializer<CommandRequest> {

    public CommandRequestDeserializer() {
        this(CommandRequest.class);
    }

    protected CommandRequestDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CommandRequest deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        ObjectMapper objectMapper = (ObjectMapper) p.getCodec();

        CommandRequest.Operation operation = objectMapper.convertValue(
                node.get("operation"),
                CommandRequest.Operation.class
        );

        CommandRequest.RequestEntityType requestEntityType = objectMapper.convertValue(
                node.get("requestEntityType"),
                CommandRequest.RequestEntityType.class
        );

        Object entity = null;
        if (node.has("entity")) {
            entity = objectMapper.convertValue(
                    node.get("entity"),
                    requestEntityType.DTOclass
            );
        }

        return new CommandRequest(operation, requestEntityType, entity);
    }
}
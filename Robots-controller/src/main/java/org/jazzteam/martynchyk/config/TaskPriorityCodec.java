package org.jazzteam.martynchyk.config;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.jazzteam.martynchyk.tasks.TaskPriority;

public class TaskPriorityCodec implements Codec<TaskPriority> {

    @Override
    public void encode(final BsonWriter writer, final TaskPriority value, final EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("name");
        writer.writeString(value.name());

        writer.writeName("priority");
        writer.writeInt32(value.priority);

        writer.writeEndDocument();
    }

    @Override
    public TaskPriority decode(final BsonReader reader, final DecoderContext decoderContext) {
        reader.readStartDocument();

        reader.readName();
        String name=reader.readString();
        reader.readName();
        reader.readInt32();

        reader.readEndDocument();
        return TaskPriority.valueOf(name);
    }

    @Override
    public Class<TaskPriority> getEncoderClass() {
        return TaskPriority.class;
    }
}

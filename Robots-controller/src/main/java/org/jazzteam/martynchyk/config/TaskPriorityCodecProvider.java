package org.jazzteam.martynchyk.config;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.jazzteam.martynchyk.tasks.TaskPriority;

public class TaskPriorityCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> type, CodecRegistry cr) {
        if (type == TaskPriority.class) {
            return (Codec<T>) new TaskPriorityCodec();
        }
        return null;
    }
}

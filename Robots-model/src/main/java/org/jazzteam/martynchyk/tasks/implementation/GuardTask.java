package org.jazzteam.martynchyk.tasks.implementation;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.jazzteam.martynchyk.tasks.BaseTask;

@Data
@BsonDiscriminator
public class GuardTask extends BaseTask {
}

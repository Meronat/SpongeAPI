package org.spongepowered.api.data.manipulator.mutable.entity;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.type.BoatStatus;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.data.value.mutable.Value;

public interface BoatData extends DataManipulator {

    Value<Boolean> moveOnLand();

    Value<BoatStatus> boatStatus();

    MutableBoundedValue<Double> maxSpeed();

    MutableBoundedValue<Double> momentum();

    Value<Double> outOfControlTicks();

    Value<Double> ejectOutOfControlTicks();

    MutableBoundedValue<Double> damageTaken();

    MutableBoundedValue<Double> damageBeforeDestruction();

}

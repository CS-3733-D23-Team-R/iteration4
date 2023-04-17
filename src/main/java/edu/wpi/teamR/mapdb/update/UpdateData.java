package edu.wpi.teamR.mapdb.update;

import java.lang.reflect.Method;

public record UpdateData(Method method, Object[] args) {
}

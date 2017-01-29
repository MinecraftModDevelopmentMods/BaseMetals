package com.mcmoddev.basemetals.util;

import astro.lib.Lib;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * From AstroLib
 *
 * @author TheCodedOne
 */
public class AnnotationChecker {

    public static <T> List<T> getInstances(ASMDataTable asmDataTable, Class<? extends Annotation> annotationClass, Class<T> instanceClass) {
        String annotationClassName = annotationClass.getCanonicalName();
        Set<ASMDataTable.ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
        List<T> instances = new ArrayList<>();
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                instances.add(Class.forName(asmData.getClassName()).asSubclass(instanceClass).newInstance());
            } catch (Exception e) {
                Lib.LOG.error("Failed to load: {}", asmData.getClassName(), e);
            }
        }
        return instances;
    }
}
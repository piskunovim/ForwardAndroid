package ru.forwardmobile.tforwardpayment.spp.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IProvider;

/**
 * Plain Old Java Object реализация провайдера
 * @author Vasiliy Vanin
 */
public class ProviderPojoImpl implements IProvider {    

    final Integer id;
    final Double maxSumm;
    final Double minSumm;
    final Set<IField> fields = new HashSet<IField>();
    final String name;
    
    public ProviderPojoImpl(String name, Integer id, Double maxSumm, Double minSumm) {
        this(name, id, maxSumm, minSumm, null);
    }
    
    public ProviderPojoImpl(String name, Integer id, Double maxSumm, Double minSumm, Collection<IField> fields) {
        
        this.name = name;
        this.id = id;
        this.maxSumm = maxSumm;
        this.minSumm = minSumm;
        
        if( fields != null)
            this.fields.addAll(fields);
    }
    
    public void addField(IField field) {
        fields.add(field);
    }
    
    public Integer getId() {
        return id;
    }

    public Double getMaxSumm() {
        return maxSumm;
    }

    public Double getMinSumm() {
        return minSumm;
    }

    public Collection<IField> getFields() {
        return fields;
    }
    
    @Override
    public String toString() {
        return name;
    }
}

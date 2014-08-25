package ru.forwardmobile.tforwardpayment.operators.impl;

import java.util.ArrayList;
import java.util.Collection;
import ru.forwardmobile.tforwardpayment.operators.IProvider;
import ru.forwardmobile.tforwardpayment.spp.IFieldInfo;

/**
 * @author Василий Ванин
 */
public class ProviderImpl implements IProvider {

    private Integer     id;
    private String      name;
    private Double      maxLimit = 14999d;
    private Double      minLimit = 1d;
    
    private Collection<IFieldInfo>  fields
                = new ArrayList<IFieldInfo>();
    
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<IFieldInfo> getFields() {
        return fields;
    }

    @Override
    public Double getMaxLimit() {
        return maxLimit;
    }

    @Override
    public Double getMinLimit() {
        return minLimit;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }

    public void setMinLimit(Double minLimit) {
        this.minLimit = minLimit;
    }

    public void setFields(Collection<IFieldInfo> fields) {
        this.fields = fields;
    }
    
}

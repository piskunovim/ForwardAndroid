package ru.forwardmobile.tforwardpayment.operators.impl;

import java.util.ArrayList;
import java.util.Collection;

import ru.forwardmobile.tforwardpayment.operators.IProcessor;
import ru.forwardmobile.tforwardpayment.spp.IField;
import ru.forwardmobile.tforwardpayment.spp.IProvider;


/**
 * @author Василий Ванин
 */
public class ProviderImpl implements IProvider {

    private Integer     id;
    private String      name;
    private Double      maxLimit = 14999d;
    private Double      minLimit = 1d;

    private IProcessor  processor = null;
    
    private Collection<IField>  fields
                = new ArrayList<IField>();

    @Override
    public IProcessor getProcessor() {
        return processor;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<IField> getFields() {
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

    public void setFields(Collection<IField> fields) {
        this.fields = fields;
    }

    public void addField(IField field) {
        this.fields.add(field);
    };

    public void setProcessor(IProcessor processor) {
        this.processor = processor;
    }
}

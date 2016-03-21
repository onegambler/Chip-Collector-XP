package com.chipcollector.data.configuration;

import com.avaje.ebean.EbeanServer;
import com.chipcollector.domain.Property;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.PropertyConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.isNull;

public class EbeanConfiguration extends AbstractConfiguration {

    private final EbeanServer ebeanServer;
    private static final String KEY_COLUMN = "key";

    public EbeanConfiguration(EbeanServer server) {
        this.ebeanServer = server;
    }

    @Override
    protected void addPropertyDirect(String key, Object value) {

        Property property = ebeanServer.find(Property.class).where().eq(KEY_COLUMN, key).findUnique();

        if (isNull(property)) {
            property = new Property(key);
        }

        property.setValue(value.toString());
        ebeanServer.save(property);
    }

    @Override
    public boolean isEmpty() {
        return ebeanServer.createQuery(Property.class).findRowCount() == 0;
    }

    @Override
    public boolean containsKey(String key) {
        return ebeanServer.find(Property.class).where().eq(KEY_COLUMN, key).findRowCount() > 0;
    }

    @Override
    public Object getProperty(String keyName) {
        final Property property = ebeanServer.createQuery(Property.class)
                .where().eq(KEY_COLUMN, keyName).findUnique();

        final List<Object> result = new ArrayList<>();
        Optional.ofNullable(property).map(Property::getValue)
                .ifPresent(value -> {
                    if (isDelimiterParsingDisabled()) {
                        result.add(value);
                    } else {

                        Iterator<?> iterator = PropertyConverter.toIterator(value, this.getListDelimiter());
                        result.addAll(newArrayList(iterator));

                    }
                });

        return result.size() == 1 ? result.get(0) : result;

    }

    @Override
    public Iterator<String> getKeys() {
        return ebeanServer.find(Property.class).findList().stream().map(Property::getKey).iterator();
    }
}

package nl.vpro.io.prepr;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.MCItems;
import nl.vpro.io.prepr.domain.MCPerson;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Named
public class PreprPersonsImpl implements PreprPersons {

    private  final PreprRepositoryClient impl;


    private final Fields PERSON_FIELDS = Fields.builder()
        .f("description")
        .f("biography")
        .f("age")
        .f("date_of_birth")
        .f("picture")
        .f("email")
        .f("phone")
        .f("tags")
        .f("social_media_handle")
        .build();


    @Inject
    public PreprPersonsImpl(PreprRepositoryClient impl) {
        this.impl = impl;
    }

    @Override
    public MCPerson get(UUID uuid) {
        GenericUrl url = impl.createUrl("persons", uuid.toString());
        return impl.get(url, MCPerson.class);
    }

    @Override
    public MCItems<MCPerson> list(String q) {

        GenericUrl url = impl.createUrl("persons");
        url.set("q", q);
        url.set("fields", PERSON_FIELDS);
        return impl.get(url, MCItems.class);


    }
}

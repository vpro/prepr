package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URI;
import java.util.List;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PreprChannel extends PreprContent {


    String name;

    String username;

    String screen_name;

    URI profile_pic;

    URI picture;

    String type;

    URI share_construct_url;

    URI preview_construct_url;

    URI share_url;

    List<PreprAsset> assets;

    boolean messages;



}

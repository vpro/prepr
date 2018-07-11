package nl.vpro.io.mediaconnect.domain;

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
public class MCChannel extends MCContent {


    String name;

    String username;

    String screen_name;

    URI profile_pic;

    URI picture;

    String type;

    URI share_construct_url;

    URI preview_construct_url;

    URI share_url;

    List<MCAsset> assets;



}

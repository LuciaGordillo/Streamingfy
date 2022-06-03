package com.streamingfy.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.streamingfy.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerfilesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Perfiles.class);
        Perfiles perfiles1 = new Perfiles();
        perfiles1.setId(1L);
        Perfiles perfiles2 = new Perfiles();
        perfiles2.setId(perfiles1.getId());
        assertThat(perfiles1).isEqualTo(perfiles2);
        perfiles2.setId(2L);
        assertThat(perfiles1).isNotEqualTo(perfiles2);
        perfiles1.setId(null);
        assertThat(perfiles1).isNotEqualTo(perfiles2);
    }
}

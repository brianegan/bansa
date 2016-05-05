package com.brianegan.bansaDevTools

import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.Test

class DevToolsActionTest {
    @Test
    fun testtestEqualsAndHashCode() {
        EqualsVerifier.forClass(DevToolsAction::class.java).verify()
    }
}

package com.brianegan.bansa

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Created by Dario on 3/18/2016.
 * code ported from https://github.com/reactjs/reselect/blob/master/test/test_selector.js
 * and expanded
 * TODO: support
 */


class ReselectTest {
    data class StateA(val a: Int)

    @Test
    fun basicSelectorTest() {
        val selector = SelectorFor<StateA>().withSingleField { a }
        val state = StateA(0)
        assertThat(selector(state)).isEqualTo(0)
        assertThat(selector(state)).isEqualTo(0)
        assertThat(selector.recomputations).isEqualTo(1)
        assertThat(selector(state.copy(a = 1))).isEqualTo(1)
        assertThat(selector.recomputations).isEqualTo(2)
    }

    data class StateAB(val a: Int, val b: Int)

    @Test
    fun basicSelectorWithMultipleKeysTest() {
        val selector = SelectorFor<StateAB>()
                .withField { a }
                .withField { b }
                .compute { a: Int, b: Int -> a + b }
        val state1 = StateAB(a = 1, b = 2)
        assertThat(selector(state1)).isEqualTo(3)
        assertThat(selector(state1)).isEqualTo(3)
        assertThat(selector.recomputations).isEqualTo(1)
        val state2 = StateAB(a = 3, b = 2)
        assertThat(selector(state2)).isEqualTo(5)
        assertThat(selector(state2)).isEqualTo(5)
        assertThat(selector.recomputations).isEqualTo(2)
    }

    data class StateSubStateA(val sub: StateA)

    @Test
    fun memoizedCompositeArgumentsTest() {
        val selector = SelectorFor<StateSubStateA>()
                .withField { sub }
                .compute { sub: StateA -> sub }
        val state1 = StateSubStateA(StateA(1))
        assertThat(selector(state1)).isEqualTo(StateA(1))
        assertThat(selector(state1)).isEqualTo(StateA(1))
        assertThat(selector.recomputations).isEqualTo(1)
        val state2 = StateSubStateA(StateA(2))
        assertThat(selector(state2)).isEqualTo(StateA(2))
        assertThat(selector.recomputations).isEqualTo(2)
    }





    @Test
    fun chainedSelectorTest() {
        val selector1 = SelectorFor<StateSubStateA>()
                .withField { sub }
                .compute { sub: StateA -> sub }
        val selector2 = SelectorFor<StateSubStateA>()
                .withSelector(selector1)
                .compute { sub: StateA -> sub.a }
        val state1 = StateSubStateA(StateA(1))
        assertThat(selector2(state1)).isEqualTo(1)
        assertThat(selector2(state1)).isEqualTo(1)
        assertThat(selector2.recomputations).isEqualTo(1)
        val state2 = StateSubStateA(StateA(2))
        assertThat(selector2(state2)).isEqualTo(2)
        assertThat(selector2.recomputations).isEqualTo(2)
    }


    @Test
    fun recomputationsCountTest() {
        val selector = SelectorFor<StateA>()
                .withField { a }
                .compute { a: Int -> a }

        val state1 = StateA(a = 1)
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector.recomputations).isEqualTo(1)
        val state2 = StateA(a = 2)
        assertThat(selector(state2)).isEqualTo(2)
        assertThat(selector.recomputations).isEqualTo(2)

        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector.recomputations).isEqualTo(3)
        assertThat(selector(state2)).isEqualTo(2)
        assertThat(selector.recomputations).isEqualTo(4)
    }

    @Test
    fun isChangedTest() {
        val selector = SelectorFor<StateA>()
                .withField { a }
                .compute { a: Int -> a }
        val state1 = StateA(a = 1)
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector.isChanged()).isTrue()
        selector.resetChanged()
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector.isChanged()).isFalse()
        val state2 = StateA(a = 2)
        assertThat(selector(state2)).isEqualTo(2)
        assertThat(selector.isChanged()).isTrue()
    }

    data class State3(val p1: Double, val p2: Double, val p3: Double)

    @Test
    fun args3Test() {
        val selector = SelectorFor<State3>()
                .withField { p1 }
                .withField { p2 }
                .withField { p3 }
                .compute { p1: Double, p2: Double, p3: Double -> p1 / p2 / p3 }
        val state = State3(1.0, 2.0, 3.0)
        assertThat(selector(state)).isEqualTo(1.0 / 2.0 / 3.0)
    }

    data class State4(val p1: Double, val p2: Double, val p3: Double, val p4: Double)

    @Test
    fun args4Test() {
        val selector = SelectorFor<State4>()
                .withField { p1 }
                .withField { p2 }
                .withField { p3 }
                .withField { p4 }
                .compute { p1: Double, p2: Double, p3: Double, p4: Double -> p1 / p2 / p3 / p4 }
        val state = State4(1.0, 2.0, 3.0, 4.0)
        assertThat(selector(state)).isEqualTo(1.0 / 2.0 / 3.0 / 4.0)
    }

    data class State5(val p1: Double, val p2: Double, val p3: Double, val p4: Double, val p5: Double)

    @Test
    fun args5Test() {
        val selector = SelectorFor<State5>()
                .withField { p1 }
                .withField { p2 }
                .withField { p3 }
                .withField { p4 }
                .withField { p5 }
                .compute { p1: Double, p2: Double, p3: Double, p4: Double, p5: Double -> p1 / p2 / p3 / p4 / p5 }

        val state = State5(1.0, 2.0, 3.0, 4.0, 5.0)
        assertThat(selector(state)).isEqualTo(1.0 / 2.0 / 3.0 / 4.0 / 5.0)
    }

    @Test
    fun singleFieldSelectorTest() {
        val sel4state = SelectorFor<State3>()
        val selp1 = sel4state.withSingleField { p1 }
        val selp2 = sel4state.withSingleField { p2 }
        val selp3 = sel4state.withSingleField { p3 }

        val state = State3(1.0, 2.0, 3.0)
        assertThat(selp1(state)).isEqualTo(1.0)
        assertThat(selp2(state)).isEqualTo(2.0)
        assertThat(selp3(state)).isEqualTo(3.0)
    }

    /*
        //test for short syntax for single field selector disabled because of kotlin compiler bug
        @Test
        fun singleFieldSelectorShortSyntaxText() {
            val sel4state = SelectorFor<State3>()
            val selp1 = sel4state{ p1 }
            val selp2 = sel4state{ p2 }
            val selp3 = sel4state{ p3 }

            val state = State3(1.0, 2.0, 3.0)
            assertThat(selp1(state)).isEqualTo(1.0)
            assertThat(selp2(state)).isEqualTo(2.0)
            assertThat(selp3(state)).isEqualTo(3.0)
        }
        */
    @Test
    fun onChangeTest() {
        val sel_a = SelectorFor<StateA>().withSingleField { a }
        val state = StateA(a = 0)
        assertThat(sel_a(state)).isEqualTo(0)
        val changedState = state.copy(a = 1)
        var firstChangedA: Int? = null
        sel_a.onChangeIn(changedState) {
            firstChangedA = it
        }
        var secondChangedA: Int? = null
        sel_a.onChangeIn(changedState) {
            secondChangedA = it
        }
        assertThat(firstChangedA).isEqualTo(1)
        assertThat(secondChangedA).isNull()

    }
}



package com.brianegan.bansa
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
/**
 * Created by Dario on 3/18/2016.
 * code ported from https://github.com/reactjs/reselect/blob/master/test/test_selector.js
 * and expanded
 */



class ReselectTest {
    data class StateA(val a:Int)
    @Test
    fun basicSelectorTest(){
        val selector = SelectorFor<StateA>().withSingleField{a}
        val state = StateA(0)
        assertThat(selector(state)).isEqualTo(0)
        assertThat(selector(state)).isEqualTo(0)
        assertThat(selector.recomputations).isEqualTo(1)
        assertThat(selector(state.copy(a=1))).isEqualTo(1)
        assertThat(selector.recomputations).isEqualTo(2)
    }
    data class StateAB(val a:Int, val b:Int)
    @Test
    fun basicSelectorWithMultipleKeysTest() {
        val selector= SelectorFor<StateAB>()
                .withField{a}
                .withField{b}
                .compute{a:Int,b:Int -> a+b}
        val state1= StateAB(a=1,b=2)
        assertThat(selector(state1)).isEqualTo(3)
        assertThat(selector(state1)).isEqualTo(3)
        assertThat(selector.recomputations).isEqualTo(1)
        val state2=StateAB(a=3,b=2)
        assertThat(selector(state2)).isEqualTo(5)
        assertThat(selector(state2)).isEqualTo(5)
        assertThat(selector.recomputations).isEqualTo(2)
    }

    data class StateSubStateA (val sub:StateA)
    @Test
    fun MemoizedCompositeArgumentsTest() {
        val selector = SelectorFor<StateSubStateA>()
                .withField {sub}
                .compute {sub:StateA -> sub}
        val state1= StateSubStateA(StateA(1))
        assertThat(selector(state1)).isEqualTo(StateA(1))
        assertThat(selector(state1)).isEqualTo(StateA(1))
        assertThat(selector.recomputations).isEqualTo(1)
        val state2= StateSubStateA(StateA(2))
        assertThat(selector(state2)).isEqualTo(StateA(2))
        assertThat(selector.recomputations).isEqualTo(2)
    }
    @Ignore //not implemented
    @Test
    fun FirstArgumentCanBeAnArrayTest() {
        /*
            const selector = createSelector(
      [ state => state.a, state => state.b ],
      (a, b) => {
        return a + b
      }
    )
    assert.equal(selector({ a: 1, b: 2 }), 3)
    assert.equal(selector({ a: 1, b: 2 }), 3)
    assert.equal(selector.recomputations(), 1)
    assert.equal(selector({ a: 3, b: 2 }), 5)
    assert.equal(selector.recomputations(), 2)
         */
    }

    //TODO I have not really implemented props, but not sure what is the use case.
    @Test
    fun CanAcceptPropsTest() {

        val selector = SelectorFor<StateAB>()
                .withField {a}
                .withField {b}
                .withField {100}
                .compute{a:Int,b:Int,c:Int -> a+b+c}
        val state1=StateAB(a=1,b=2)
        assertThat(selector(state1)).isEqualTo(103)

        /*
       let called = 0
       const selector = createSelector(
         state => state.a,
         state => state.b,
         (state, props) => props.c,
         (a, b, c) => {
           called++
           return a + b + c
         }
       )
       assert.equal(selector({ a: 1, b: 2 }, { c: 100 }), 103)
            */
    }

    @Test
    fun ChainedSelectorTest() {
        val selector1= SelectorFor<StateSubStateA>()
                .withField {sub}
                .compute{sub:StateA -> sub}
        val selector2= SelectorFor<StateSubStateA>()
                .withSelector(selector1)
                .compute{sub:StateA -> sub.a }
        val state1=StateSubStateA(StateA(1))
        assertThat(selector2(state1)).isEqualTo(1)
        assertThat(selector2(state1)).isEqualTo(1)
        assertThat(selector2.recomputations).isEqualTo(1)
        val state2=StateSubStateA(StateA(2))
        assertThat(selector2(state2)).isEqualTo(2)
        assertThat(selector2.recomputations).isEqualTo(2)
    }
    @Ignore //not implemented
    @Test
    fun ChainedSelectorWithProps() {
        /*
     const selector1 = createSelector(
      state => state.sub,
        (state, props) => props.x,
        (sub, x) => ({ sub, x })
    )
    const selector2 = createSelector(
      selector1,
      (state, props) => props.y,
        (param, y) => param.sub.value + param.x + y
    )
    const state1 = { sub: {  value: 1 } }
    assert.equal(selector2(state1, { x: 100, y: 200 }), 301)
    assert.equal(selector2(state1, { x: 100, y: 200 }), 301)
    assert.equal(selector2.recomputations(), 1)
    const state2 = { sub: {  value: 2 } }
    assert.equal(selector2(state2, { x: 100, y: 201 }), 303)
    assert.equal(selector2.recomputations(), 2)
         */
    }
    @Ignore //not implemented
    @Test
    fun ChainedSelectorWithVariadicArgs() {
        /*
    const selector1 = createSelector(
      state => state.sub,
        (state, props, another) => props.x + another,
        (sub, x) => ({ sub, x })
    )
    const selector2 = createSelector(
      selector1,
      (state, props) => props.y,
        (param, y) => param.sub.value + param.x + y
    )
    const state1 = { sub: {  value: 1 } }
    assert.equal(selector2(state1, { x: 100, y: 200 }, 100), 401)
    assert.equal(selector2(state1, { x: 100, y: 200 }, 100), 401)
    assert.equal(selector2.recomputations(), 1)
    const state2 = { sub: {  value: 2 } }
    assert.equal(selector2(state2, { x: 100, y: 201 }, 200), 503)
    assert.equal(selector2.recomputations(), 2)
         */
    }

    @Ignore //not implemented
    @Test
    fun overrideValueEquals() {
        /*
       // a rather absurd equals operation we can verify in tests
    const createOverridenSelector = createSelectorCreator(
      defaultMemoize,
      (a, b) => typeof a === typeof b
    )
    const selector = createOverridenSelector(
      state => state.a,
        a => a
    )
    assert.equal(selector({ a: 1 }), 1)
    assert.equal(selector({ a: 2 }), 1) // yes, really true
    assert.equal(selector.recomputations(), 1)
    assert.equal(selector({ a: 'A' }), 'A')
    assert.equal(selector.recomputations(), 2)

         */
    }

    @Ignore //not implemented
    @Test
    fun customMemoizeTest() {
        /*
       const hashFn = (...args) => args.reduce((acc, val) => acc + '-' + JSON.stringify(val))
    const customSelectorCreator = createSelectorCreator(
      lodashMemoize,
      hashFn
    )
    const selector = customSelectorCreator(
      state => state.a,
      state => state.b,
      (a, b) => a + b
    )
    assert.equal(selector({ a: 1, b: 2 }), 3)
    assert.equal(selector({ a: 1, b: 2 }), 3)
    assert.equal(selector.recomputations(), 1)
    assert.equal(selector({ a: 1, b: 3 }), 4)
    assert.equal(selector.recomputations(), 2)
    assert.equal(selector({ a: 1, b: 3 }), 4)
    assert.equal(selector.recomputations(), 2)
    assert.equal(selector({ a: 2, b: 3 }), 5)
    assert.equal(selector.recomputations(), 3)
    // TODO: Check correct memoize function was called
         */
    }

    @Ignore //not implemented
    @Test
    fun exportedMemoizeTest() {
        /*
       let called = 0
    const memoized = defaultMemoize(state => {
      called++
      return state.a
    })

    const o1 = { a: 1 }
    const o2 = { a: 2 }
    assert.equal(memoized(o1), 1)
    assert.equal(memoized(o1), 1)
    assert.equal(called, 1)
    assert.equal(memoized(o2), 2)
    assert.equal(called, 2)
         */
    }

    @Ignore //not implemented
    @Test
    fun exportedMemoizeWithMultipleArgsTest() {
        /*
            const memoized = defaultMemoize((...args) => args.reduce((sum, value) => sum + value, 0))
    assert.equal(memoized(1, 2), 3)
    assert.equal(memoized(1), 1)
         */
    }

    @Ignore //not implemented
    @Test
    fun exportedMemoizeWithValueEqualsOverride() {
        /*
     // a rather absurd equals operation we can verify in tests
    let called = 0
    const valueEquals = (a, b) => typeof a === typeof b
    const memoized = defaultMemoize(
      a => {
        called++
        return a
      },
      valueEquals
    )
    assert.equal(memoized(1), 1)
    assert.equal(memoized(2), 1) // yes, really true
    assert.equal(called, 1)
    assert.equal(memoized('A'), 'A')
    assert.equal(called, 2)
         */
    }

    @Ignore //not implemented
    @Test
    fun structuredSelectorTest() {
        /*
      const selector = createStructuredSelector({
      x: state => state.a,
      y: state => state.b
    })
    const firstResult = selector({ a: 1, b: 2 })
    assert.deepEqual(firstResult, { x: 1, y: 2 })
    assert.strictEqual(selector({ a: 1, b: 2 }), firstResult)
    const secondResult = selector({ a: 2, b: 2 })
    assert.deepEqual(secondResult, { x: 2, y: 2 })
    assert.strictEqual(selector({ a: 2, b: 2 }), secondResult)
         */
    }

    @Ignore //not implemented
    @Test
    fun structuredSelectorWithCustomSelectorCreator() {
        /*
       const customSelectorCreator = createSelectorCreator(
      defaultMemoize,
      (a, b) => a === b
    )
    const selector = createStructuredSelector({
      x: state => state.a,
      y: state => state.b
    }, customSelectorCreator)
    const firstResult = selector({ a: 1, b: 2 })
    assert.deepEqual(firstResult, { x: 1, y: 2 })
    assert.strictEqual(selector({ a: 1, b: 2 }), firstResult)
    assert.deepEqual(selector({ a: 2, b: 2 }), { x: 2, y: 2 })
         */
    }
    @Test
    fun resetComputationsTest() {
        val selector = SelectorFor<StateA>()
                .withField{ a}
                .compute{a:Int -> a}

        val state1= StateA(a=1)
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector.recomputations).isEqualTo(1)
        val state2 = StateA(a=2)
        assertThat(selector(state2)).isEqualTo(2)
        assertThat(selector.recomputations).isEqualTo(2)

        selector.resetComputations()
        assertThat(selector.recomputations).isEqualTo(0)

        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector.recomputations).isEqualTo(1)
        assertThat(selector(state2)).isEqualTo(2)
        assertThat(selector.recomputations).isEqualTo(2)
    }
    @Test
    fun isChangedTest() {
        val selector = SelectorFor<StateA>()
                .withField { a}
                .compute{a:Int -> a}
        val state1= StateA(a=1)
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector.isChanged()).isTrue()
        selector.resetChanged()
        assertThat(selector(state1)).isEqualTo(1)
        assertThat(selector.isChanged()).isFalse()
        val state2=StateA(a=2)
        assertThat(selector(state2)).isEqualTo(2)
        assertThat(selector.isChanged()).isTrue()
    }

    data class State3(val p1:Double, val p2:Double, val p3:Double)
    @Test
    fun args3Test() {
        val selector = SelectorFor<State3>()
                .withField { p1}
                .withField { p2}
                .withField { p3}
                .compute{p1:Double,p2:Double,p3:Double -> p1/p2/p3}
        val state= State3(1.0,2.0,3.0)
        assertThat(selector(state)).isEqualTo(1.0/2.0/3.0)
    }
    data class State4(val p1:Double, val p2:Double, val p3:Double,val p4:Double)
    @Test
    fun args4Test() {
        val selector = SelectorFor<State4>()
                .withField { p1}
                .withField { p2}
                .withField { p3}
                .withField { p4}
                .compute { p1:Double,p2:Double,p3:Double,p4:Double -> p1/p2/p3/p4}
        val state= State4(1.0,2.0,3.0,4.0)
        assertThat(selector(state)).isEqualTo(1.0/2.0/3.0/4.0)
    }
    data class State5(val p1:Double, val p2:Double, val p3:Double,val p4:Double,val p5:Double)
    @Test
    fun args5Test() {
        val selector = SelectorFor<State5>()
                .withField { p1}
                .withField  { p2}
                .withField  { p3}
                .withField  { p4}
                .withField  { p5}
                .compute{p1:Double,p2:Double,p3:Double,p4:Double,p5:Double -> p1/p2/p3/p4/p5}

        val state= State5(1.0,2.0,3.0,4.0,5.0)
        assertThat(selector(state)).isEqualTo(1.0/2.0/3.0/4.0/5.0)
    }

    @Test
    fun singleFieldSelectorTest() {
        val sel4state=SelectorFor<State3>()
        val selp1= sel4state.withSingleField { p1 }
        val selp2= sel4state.withSingleField { p2 }
        val selp3= sel4state.withSingleField { p3 }

        val state= State3(1.0,2.0,3.0)
        assertThat(selp1(state)).isEqualTo(1.0)
        assertThat(selp2(state)).isEqualTo(2.0)
        assertThat(selp3(state)).isEqualTo(3.0)
    }
    @Test
    fun onChangeTest() {
        val sel_a= SelectorFor<StateA>().withSingleField { a }
        val state=StateA(a=0)
        assertThat(sel_a(state)).isEqualTo(0)
        val changedState = state.copy(a=1)
        var firstChangedA:Int?=null
        sel_a.onChangeIn(changedState) {
            firstChangedA=it
        }
        var secondChangedA:Int?=null
        sel_a.onChangeIn(changedState) {
            secondChangedA=it
        }
        assertThat(firstChangedA).isEqualTo(1)
        assertThat(secondChangedA).isNull()

    }
}



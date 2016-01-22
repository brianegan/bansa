package com.brianegan.bansa.listOfCounters

import trikita.anvil.RenderableAdapter

class BansaAdapter<M, VM>(var models: List<M>, val mapModelToViewModel: (M) -> VM, val r: RenderableAdapter.Item<VM>) : RenderableAdapter() {
    override fun getCount(): Int {
        return models.size
    }

    override fun getItem(position: Int): VM {
        return mapModelToViewModel(models[position])
    }

    override fun view(pos: Int) {
        r.view(pos, this.getItem(pos));
    }

    fun update(newModels: List<M>): BansaAdapter<M, VM> {
        if (models.equals(newModels).not()) {
            this.models = newModels;
            notifyDataSetChanged()
        }

        return this
    }
}

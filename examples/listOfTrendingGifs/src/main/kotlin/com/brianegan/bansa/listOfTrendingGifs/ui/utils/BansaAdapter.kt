package com.brianegan.bansa.listOfTrendingGifs.ui.utils

import trikita.anvil.RenderableAdapter

class BansaAdapter<M, VM>(var models: List<M>, val mapModelToViewModel: (M) -> VM, val render: (VM) -> Unit) : RenderableAdapter() {
    override fun getCount(): Int {
        return models.size
    }

    override fun getItem(position: Int): VM {
        return mapModelToViewModel(models[position])
    }

    override fun view(pos: Int) {
        render(this.getItem(pos));
    }

    fun update(newModels: List<M>): BansaAdapter<M, VM> {
        if (models.equals(newModels).not()) {
            this.models = newModels;
            notifyDataSetChanged()
        }

        return this
    }
}

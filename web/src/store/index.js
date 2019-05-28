/**
 *Created by 吕珊 on 2019/3/4.
 */



import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);


const state = {
    LOADING: false
};

export default new Vuex.Store({
    state: state,
    getters: {},
    mutations: {
        changeLoading(state,data){
            state.LOADING = data
        },

    }
})
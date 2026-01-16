import { describe, it, expect, vi } from "vitest"
import { mount } from "@vue/test-utils"
import ToDoList from "./ToDoList.vue"

describe("ToDoList", () => {
    it("rendert Suchfeld und Add-Button", async () => {
        global.fetch = vi.fn(async () => ({
            ok: true,
            json: async () => [],
        }))

        const wrapper = mount(ToDoList)

        expect(wrapper.find('input[placeholder="Suchen…"]').exists()).toBe(true)
        expect(wrapper.text()).toContain("Hinzufügen")
    })
})

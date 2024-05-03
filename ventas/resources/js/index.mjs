// los import locales de JS tienen rutas relativas a la ruta del script que hace el enrutamiento
import * as Popper from '../utils/popper/popper.min.js'
import * as bootstrap from '../utils/bootstrap-5.3.3/js/bootstrap.bundle.min.js'
import { TabulatorFull as Tabulator } from '../utils/tabulator-6.2/js/tabulator_esm.min.js'
import { DateTime, Duration } from '../utils/luxon3x.min.js'
import icons from '../utils/own/icons.js'
import Helpers from '../utils/own/helpers.js'
import Popup from '../utils/own/popup.js'
import Toast from '../utils/own/toast.js'

class App {
  static async main() {
    // Ver: https://javascript.info/browser-environment (DOM|BOM|JavaScript)
    // Las clases importadas se asignan a referencias de la ventana actual:
    window.icons = icons
    window.DateTime = DateTime
    window.Duration = Duration
    window.Helpers = Helpers
    window.Tabulator = Tabulator
    window.Toast = Toast
    window.Modal = Popup
    window.current = null // miraremos si se requiere...
    // lo siguiente es para estandarizar el estilo de los botones usados para add, edit y delete en las tablas
    window.addRowButton = `<button id='add-row' class='btn btn-info btn-sm'>${icons.plusSquare}&nbsp;&nbsp;Nuevo registro</button>`
    window.editRowButton = () => `<button id="edit-row" class="border-0 bg-transparent" data-bs-toggle="tooltip" title="Editar">${icons.edit}</button>`
    window.deleteRowButton = () => `<button id="delete-row" class="border-0 bg-transparent" data-bs-toggle="tooltip" title="Eliminar">${icons.delete}</button>`
    // lo siguiente es para estandarizar los botones de los formularios
    window.addButton = `${icons.plusSquare}&nbsp;&nbsp;<span>Agregar</span>`
    window.editButton = `${icons.editWhite}&nbsp;&nbsp;<span>Actualizar</span>`
    window.deleteButton = `${icons.deleteWhite}<span>Eliminar</span>`
    window.cancelButton = `${icons.xLg}<span>Cancelar</span>`
    window.tableHeight = 'calc(100vh - 190px)' // la altura de todos los elementos de tipo Tabulator que mostrará la aplicación

    try {
      document.querySelector('#app-logo').innerHTML = icons.market
      // los recursos locales usan rutas relativas empezando por la carpeta principal del proyecto
      const config = await Helpers.fetchData('./resources/assets/config.json')
      // evite siempre los datos quemados en el código
      window.urlAPI = config.url

      // confirmación de acceso a la API REST
      const response = await Helpers.fetchData(`${urlAPI}/`)
      if (response.message === 'ok') {
        Toast.show({ title: 'Hola', message: response.data })
      } else {
        Toast.show({ message: 'Problemas con el servidor de datos', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Falló la conexión con el servidor de datos', mode: 'danger', error: e })
    }

    // acceder a las opciones del menú
    const listOptions = document.querySelectorAll('#main-menu a')
    listOptions.forEach(item => item.addEventListener('click', App.#mainMenu))
  }

  /**
   * Determina la acción a llevar a cabo según la opción elegida en el menú principal
   * @param {String} option El texto del ancla seleccionada
   */
  static #mainMenu = async e => {
    let option = 'ninguna'
    if (e !== undefined) {
      e.preventDefault()
      option = e.target.text.trim() // <-- Importante!!!
    }

    switch (option) {
      case 'Inicio':
        document.querySelector('main').innerText = ''
        break
      case 'Productos':
        const { default: Productos } = await import('./productos.js')
        Productos.init()
      case 'Ventas':
        console.info('Submenú ventas')
        break
      case 'Clientes':
        const { default: clientes } = await import('./cliente.js')
        clientes.init('cliente')
        break
      case 'Proveedores':
        const { default: provedor } = await import('./provedor.js')
        provedor.init('provedor')
        break
      case 'Vendedores':
        const { default: vendedor } = await import('./vendedor.js')
        vendedor.init('vendedor')
        break
      case 'Acerca de...':
        Toast.show({ message: `No implementada la opción de ${option}`, mode: 'warning' })
        break
      default:
        new Toast({ content: `La opción ${option} no ha sido implementada`, mode: 'warning', delay: 3000, close: false })
        console.warn('Fallo en ', e.target)
    }
  }
}

App.main()

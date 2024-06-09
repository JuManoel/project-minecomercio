export default class bajas {
    static #table
    static #modal
    static #currentOption
    static #form
    static #tipos
    static #producto
    static #idProductos
  
    constructor() {
      throw new Error('No utilice el constructor. Use bajas.init()')
    }
  
    static async init() {
      try {
        // intentar cargar los datos del enum tipoBaja
        bajas.#tipos = await Helpers.fetchData(`${urlAPI}/baja/categoriasBaja`)
        if (bajas.#tipos.status === 404) {
          throw new Error(bajas.#tipos.title)
        }
        const productos = await Helpers.fetchData(`${urlAPI}/producto`)
        console.log(productos)
        if(productos.status === 404){
            throw new Error(bajas.#producto.title)
        }
        bajas.#producto = []
        bajas.#idProductos = []
        let i = 0
        productos.data.forEach(prod => {
            bajas.#producto[i]=prod.descripcion
            bajas.#idProductos[i]=prod.id
            i+=1
        });
        // intentar cargar el formulario de edición de bajas
        bajas.#form = await Helpers.loadPage('./resources/html/bajas.html')     
  
        // intentar cargar los datos de los bajas
        const response = await Helpers.fetchData(`${urlAPI}/baja`)
        console.log(response.data)
        if (response.message !== 'ok') {
          throw new Error(response.message)
        }
  
        // agregar al <main> de index.html la capa que contendrá la tabla
        document.querySelector('main').innerHTML = `
          <div class="p-2 w-full">
              <div id="table-container" class="m-2"></div>
          </dv>`
  
        // ver en https://tabulator.info/docs/6.2/columns#definition cómo se definen las propiedades de las columnas
        // ver en https://tabulator.info/docs/6.2/format los diferentes valores de la propiedad formater de las columnas
        bajas.#table = new Tabulator('#table-container', {
          height: tableHeight, // establecer la altura de la tabla, esto habilita el DOM virtual y mejora drásticamente la velocidad de procesamiento
          data: response.data, // asignar los datos a la tabla
          layout: 'fitColumns', // ajustar columnas al ancho cantidad
  
          columns: [
            // definir las columnas de la tabla
            { title: 'CÓDIGO', field: 'id', width: 100, hozAlign: 'center' },
            { title: 'NOMBRE', field: 'producto.descripcion', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
            { title: 'TIPO', field: 'tipoBaja', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
            { title: 'DISP.', field: 'cantidad', width: 70, hozAlign: 'left', hozAlign: 'center' },
            { title: 'VENCIMIENTO', field: 'producto.vencimiento', width: 135, hozAlign: 'center' },
            { title: 'Vr. BASE', field: 'producto.valorBase', width: 110, hozAlign: 'center', formatter: 'money' },
            { title: 'Vr. VENTA', field: 'producto.valorVenta', width: 110, hozAlign: 'center', formatter: 'money' },
          ],
          // mostrar al final de la tabla un botón para agregar registros
          footerElement: `<div class='container-fluid d-flex justify-content-end p-0'>${addRowButton}</div>`,
        })
  
        // agregar un gestor de eventos al botón 'add-row' para mostrar el formulario en donde se ingresarán bajas
        bajas.#table.on('tableBuilt', () => document.querySelector('#add-row').addEventListener('click', bajas.#addRow))
      } catch (e) {
        Toast.show({ title: 'Ventas', message: e.message, mode: 'danger', error: e })
      }
  
      return this
    }
    static async #addRow() {
      bajas.#currentOption = 'add'
      bajas.#modal = new Modal({
        classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
        title: '<h5>Ingreso de bajas</h5>',
        content: bajas.#form,
        buttons: [
          { caption: addButton, classes: 'btn btn-primary me-2', action: () => bajas.#add() },
          { caption: cancelButton, classes: 'btn btn-secondary', action: () => bajas.#modal.close() },
        ],
        doSomething: bajas.#toComplete,
      })
      bajas.#modal.show()
    }
  
    static async #add() {
      try {
        // verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
        if (!Helpers.okForm('#form-bajas')) {
          return
        }
        // obtener del formulario el objeto con los datos que se envían a la solicitud POST
        const body = bajas.#getFormData()
  
        // enviar la solicitud de creación con los datos del formulario
        let response = await Helpers.fetchData(`${urlAPI}/baja`, {
          method: 'POST',
          body,
        })
  
        if (response.message === 'ok') {
          bajas.#table.addRow(response.data) // agregar el baja a la tabla
          bajas.#modal.close()
          Toast.show({ message: 'baja agregado exitosamente' })
        } else {
          Toast.show({ message: 'No se pudo agregar el baja', mode: 'danger', error: response })
        }
      } catch (e) {
        Toast.show({ message: 'Falló la operación de creación del baja', mode: 'danger', error: e })
      }
    }
  
    static #toComplete(idModal, rowData) {
      // crear una lista de opciones a partir del enum tipoBaja
      const tipos = Helpers.toOptionList({
        items: bajas.#tipos.data,
        value: 'key',
        text: 'value',
        selected: bajas.#currentOption === 'edit' ? rowData.tipo : '',
      })
  
      // asignar la lista de opciones al select "tipo" de bajas.html
      document.querySelector(`#${idModal} #tipo`).innerHTML = tipos
  
      if (bajas.#currentOption === 'edit') {
        // mostrar los datos de la fila actual en el formulario html
        document.querySelector(`#${idModal} #id`).value = rowData.id
        document.querySelector(`#${idModal} #descripcion`).value = rowData.descripcion
        document.querySelector(`#${idModal} #cantidad`).value = rowData.cantidad
        document.querySelector(`#${idModal} #vencimiento`).value = rowData.vencimiento
        document.querySelector(`#${idModal} #valor-base`).value = rowData.valorBase
        document.querySelector(`#${idModal} #valor-venta`).value = rowData.valorVenta
        document.querySelector(`#${idModal} #iva`).value = rowData.iva
      }
    }
  
    /**
     * Recupera los datos del formulario y crea un objeto para ser retornado
     * @returns Un objeto con los datos del baja
     */
    static #getFormData() {
      const id = document.querySelector(`#${bajas.#modal.id} #id`).value
      const descripcion = document.querySelector(`#${bajas.#modal.id} #descripcion`).value
      const cantidad = document.querySelector(`#${bajas.#modal.id} #cantidad`).value
      const vencimiento = document.querySelector(`#${bajas.#modal.id} #vencimiento`).value
      const valorBase = document.querySelector(`#${bajas.#modal.id} #valor-base`).value
      const valorVenta = document.querySelector(`#${bajas.#modal.id} #valor-venta`).value
      const tipo = document.querySelector(`#${bajas.#modal.id} #tipo`).value
      const iva = document.querySelector(`#${bajas.#modal.id} #iva`).value
  
      return { id, descripcion, tipo, valorBase, valorVenta, iva, cantidad, vencimiento }
    }
  }
  
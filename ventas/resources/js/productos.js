export default class Productos {
  static #table
  static #modal
  static #currentOption
  static #form
  static #tipos

  constructor() {
    throw new Error('No utilice el constructor. Use Productos.init()')
  }

  static async init() {
    try {
      // intentar cargar los datos del enum TipoProducto
      Productos.#tipos = await Helpers.fetchData(`${urlAPI}/producto/categorias`)
      if (Productos.#tipos.status === 404) {
        throw new Error(Productos.#tipos.title)
      }

      // intentar cargar el formulario de edición de productos
      Productos.#form = await Helpers.loadPage('./resources/html/productos.html')

      // intentar cargar los datos de los productos
      const response = await Helpers.fetchData(`${urlAPI}/producto`)
      console.log(response.data)
      if (response.message !== 'ok') {
        throw new Error(response.message)
      }
      // agregar al <main> de index.html la capa que contendrá la tabla
      document.querySelector('main').innerHTML = `
        <div class="p-2 w-full">
            <div id="table-container" class="m-2"></div>
        </div>`

      // ver en https://tabulator.info/docs/6.2/columns#definition cómo se definen las propiedades de las columnas
      // ver en https://tabulator.info/docs/6.2/format los diferentes valores de la propiedad formater de las columnas
      Productos.#table = new Tabulator('#table-container', {
        height: tableHeight, // establecer la altura de la tabla, esto habilita el DOM virtual y mejora drásticamente la velocidad de procesamiento
        data: response.data, // asignar los datos a la tabla
        layout: 'fitColumns', // ajustar columnas al ancho disponible
        
        columns: [
          // definir las columnas de la tabla
          { title: 'CÓDIGO', field: 'id', width: 100, hozAlign: 'center' },
          { title: 'NOMBRE', field: 'descripcion', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
          { title: 'TIPO', field: 'tipo', hozAlign: 'left' }, // No se indica width, utilizar el ancho remanente
          { title: 'DISP.', field: 'disponible', width: 70, hozAlign: 'left', hozAlign: 'center' },
          { title: 'VENCIMIENTO', field: 'vencimiento', width: 135, hozAlign: 'center' },
          { title: 'Vr. BASE', field: 'valorBase', width: 110, hozAlign: 'center', formatter: 'money' },
          { title: 'Vr. VENTA', field: 'valorVenta', width: 110, hozAlign: 'center', formatter: 'money' },
          { formatter: editRowButton, width: 40, hozAlign: 'center', cellClick: Productos.#editRowClick },
          { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: Productos.#deleteRowClick },
        ],
        // mostrar al final de la tabla un botón para agregar registros
        footerElement: `<div class='container-fluid d-flex justify-content-end p-0'>${addRowButton}</div>`,
      })

      // agregar un gestor de eventos al botón 'add-row' para mostrar el formulario en donde se ingresarán productos
      Productos.#table.on('tableBuilt', () => document.querySelector('#add-row').addEventListener('click', Productos.#addRow))
    } catch (e) {
      Toast.show({ title: 'Ventas', message: e.message, mode: 'danger', error: e })
    }

    return this
  }

  static async #addRow() {
    Productos.#currentOption = 'add'
    Productos.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: '<h5>Ingreso de productos</h5>',
      content: Productos.#form,
      buttons: [
        { caption: addButton, classes: 'btn btn-primary me-2' },
        { caption: cancelButton, classes: 'btn btn-secondary' },
      ],
      doSomething: Productos.#toComplete,
    })

    // mostrar el cuadro de diálogo y gestionar las opciones de agregar y cancelar
    try {
      const option = await Productos.#modal.show()
      if (option === 'Cancelar' || option === '✖') {
        Productos.#modal.close()
      } else if (option === 'Agregar') {
        Productos.#add()
      }
    } catch (e) {
      Toast({ message: 'Problemas al agregar el producto', mode: 'danger', error: e })
    }
  }

  static async #add() {
    // verificar si los datos cumplen con las restricciones indicadas en el formulario
    if (!Helpers.okForm('#form-productos')) {
      return
    }
  
    // obtener del formulario el objeto con los datos que se envían a la solicitud POST
    const body = Productos.#getFormData()
  
    // enviar la solicitud de creación con los datos del formulario
    let response = await Helpers.fetchData(`${urlAPI}/producto`, {
      method: 'POST',
      body,
    })
  
    if (response.message === 'ok') {
      // utilizar la data de la respuesta HTTT para agregar el producto a la tabla
      Productos.#table.addRow(response.data)
      Productos.#modal.close()
      Toast.show({ message: 'Producto agregado exitosamente' })
    } else {
      Toast.show(
        { message: 'No se pudo agregar el producto', mode: 'danger', error: response 
      })
    }
  }

  static #editRowClick = async (e, cell) => {//cell es la celda que lo llamo
    Productos.#currentOption = 'edit'
    Productos.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: '<h5>Actualización de productos</h5>',
      content:Productos.#form,
      buttons: [
        { caption: editButton, classes: 'btn btn-primary me-2' },
        { caption: cancelButton, classes: 'btn btn-secondary' },
      ],
      doSomething: idModal => Productos.#toComplete(idModal, cell.getRow().getData()),
    })
  
    try {
      const option = await Productos.#modal.show()
      if (option === 'Cancelar' || option === '✖') {
        Productos.#modal.close()
      } else if (option === 'Actualizar') {
        Productos.#edit(cell)
        
      }
    } catch (e) {
      Toast({ message: 'Problemas al modificar el producto', mode: 'danger', error: e })
    }
  }

  static async #edit(cell) {
    // verificar si los datos cumplen con las restricciones indicadas en el formulario
    console.log("loko")
    if (!Helpers.okForm('#form-productos')) {
      return
    }
  
    // // obtener el objeto con los datos que se envían a la solicitud PATCH
    const body = Productos.#getFormData()
  
    // configurar la url para enviar la solicitud PATCH
    const url = `${urlAPI}/producto/${cell.getRow().getData().id}`
  
    // intentar enviar la solicitud de actualización
    let response = await Helpers.fetchData(url, {
      method: 'PATCH',
      body
    })
  
    if (response.message === 'ok') {
      Toast.show({ message: 'Producto actualizado exitosamente' })
      cell.getRow().update(response.data)
      Productos.#modal.close()
    } else {
      Toast.show({ message: 'No se pudo actualizar el producto', mode: 'danger', error: response })
    }
  }

  static #deleteRowClick = async (e, cell) => {
    Productos.#currentOption = 'delete'
    Productos.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: '<h5>Eliminación de productos</h5>',
      content: `
        <span class="text-back dark:text-gray-300">
           Confirme la eliminación del producto:<br>
           ${cell.getRow().getData().id} - ${cell.getRow().getData().descripcion} - 
           Valor venta $${cell.getRow().getData().valorVenta}<br>
        </span>`, 
      buttons: [
        { caption: deleteButton, classes: 'btn btn-primary me-2' },
        { caption: cancelButton, classes:'btn btn-secondary' },
      ],
      // no requiere doSomething
    })
  
    try {
      const option = await Productos.#modal.show()
      console.log(option)
      if (option === 'Cancelar' || option === '✖') {
        Productos.#modal.close()
      } else if (option == 'Eliminar') {
        Productos.#delete(cell)
      }
    } catch (e) {
      Toast({ message: 'Problemas al deletar el producto', mode: 'danger', error: e })
    }
  }
  static #delete(cell) {
    console.log(cell.getRow().getData())

  }

  static #getFormData(){
    const id= document.querySelector(`#${Productos.#modal.id} #id`).value
    const descripcion= document.querySelector(`#${Productos.#modal.id} #descripcion`).value
    const disponible= document.querySelector(`#${Productos.#modal.id} #disponible`).value
    const vencimiento= document.querySelector(`#${Productos.#modal.id} #vencimiento`).value
    const valorBase= document.querySelector(`#${Productos.#modal.id} #valor-base`).value
    const valorVenta= document.querySelector(`#${Productos.#modal.id} #valor-venta`).value
    const tipo= document.querySelector(`#${Productos.#modal.id} #tipo`).value
    const iva= document.querySelector(`#${Productos.#modal.id} #iva`).value
    
    return { id, descripcion, tipo, valorBase, valorVenta, iva, disponible, vencimiento}
  }


  static #toComplete(idModal, rowData) {
    // crear una lista de opciones a partir del enum TipoProducto
    const tipos = Helpers.toOptionList({
      items: Productos.#tipos.data,
      value: 'key',
      text: 'value',
      selected: Productos.#currentOption === 'edit' ? rowData.tipo : '',
    })
  
    // asignar la lista de opciones al select "tipo" de productos.html
    document.querySelector(`#${idModal} #tipo`).innerHTML = tipos
  
    if (Productos.#currentOption === 'edit') {
      // mostrar los datos de la fila actual en el formulario html
      document.querySelector(`#${idModal} #id`).value = rowData.id
      document.querySelector(`#${idModal} #descripcion`).value = rowData.descripcion
      document.querySelector(`#${idModal} #disponible`).value = rowData.disponible
      document.querySelector(`#${idModal} #vencimiento`).value = rowData.vencimiento
      document.querySelector(`#${idModal} #valor-base`).value = rowData.valorBase
      document.querySelector(`#${idModal} #valor-venta`).value = rowData.valorVenta
      document.querySelector(`#${idModal} #iva`).value = rowData.iva
    }
  }
}

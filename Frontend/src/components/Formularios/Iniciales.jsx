import React, { useState } from 'react';
import Folio from './Folio';
import style from '../Estilos/Iniciales.module.css';

const Iniciales = () => {
    const [formData, setFormData] = useState({
        email: '',
        rfc: '',
        rfcPdf: null,
        rfcPdfName: null,
        tickets: [
            { id: Date.now(), ticketCode: '', providerCode: null }, // Folio por defecto
        ],
    });

    const [isDragging, setIsDragging] = useState(false);
    const [dragCounter, setDragCounter] = useState(0); // Contador para manejar el parpadeo

    const agregarFolio = () => {
        setFormData({
            ...formData,
            tickets: [...formData.tickets, { id: Date.now(), ticketCode: '', providerCode: null }],
        });
    };

    const eliminarFolio = (id) => {
        setFormData({
            ...formData,
            tickets: formData.tickets.filter((folio) => folio.id !== id),
        });
    };

    const actualizarFolio = (id, campo, valor) => {
        setFormData({
            ...formData,
            tickets: formData.tickets.map((folio) =>
                folio.id === id ? { ...folio, [campo]: valor } : folio
            ),
        });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleFileChange = (file) => {
        if (file) {
            const reader = new FileReader();
            reader.onload = () => {
                setFormData({
                    ...formData,
                    rfcPdf: reader.result.split(',')[1], // Codificar en Base64
                    rfcPdfName: file.name, // Guardar el nombre del archivo
                });
            };
            reader.readAsDataURL(file);
        } else {
            setFormData({
                ...formData,
                rfcPdf: null,
                rfcPdfName: null, // Limpiar el nombre del archivo si no hay archivo
            });
        }
    };

    const handleDrop = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setIsDragging(false); // Oculta el overlay
        setDragCounter(0); // Reinicia el contador

        const file = e.dataTransfer.files[0];
        if (file && file.type === 'application/pdf') {
            handleFileChange(file);
        } else {
            alert('Por favor, arrastra un archivo PDF válido.');
        }
    };

    const handleDragOver = (e) => {
        e.preventDefault();
        e.stopPropagation();
    };

    const handleDragEnter = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setDragCounter((prev) => prev + 1); // Incrementa el contador
        setIsDragging(true); // Muestra el overlay
    };

    const handleDragLeave = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setDragCounter((prev) => {
            const newCounter = prev - 1;
            if (newCounter === 0) {
                setIsDragging(false); // Oculta el overlay solo si el contador llega a 0
            }
            return newCounter;
        });
    };

    const enviarFormulario = async () => {
        const dataToSend = {
            ...formData,
            tickets: formData.tickets.map(({ id, ...rest }) => rest),
        };

        console.log('Datos del formulario:', JSON.stringify(dataToSend));
        try{
        document.cookie = await `data=${encodeURIComponent(JSON.stringify(dataToSend))}; path=/; max-age=3600`;
        // Aquí puedes enviar el JSON al endpoint
        window.location.href = '/Datos'; // Redirigir a la página principal
        } catch (error) {
            console.error('Error al enviar el formulario:', error);
            alert('Error al enviar el formulario. Por favor, inténtalo de nuevo.');
        }
    };

    return (
        <div
            className={style.fullScreenDropZone}
            onDrop={handleDrop}
            onDragOver={handleDragOver}
            onDragEnter={handleDragEnter}
            onDragLeave={handleDragLeave}
        >
            {isDragging && (
                <div className={style.dragOverlay}>
                    <p>Suelta el archivo aquí</p>
                </div>
            )}
            <div className={style.principal}>
                <label className={style.email}>
                    Ingresa tu correo electrónico:
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleInputChange}
                    />
                </label>
                <div className={style.rfcContainer}>
                    <div className={style.rfcPDF}>
                        <label>
                            Carga tu información físcal en PDF.
                            <p style={{ color: 'gray' }}>Arrastra y suelta el archivo aquí o selecciona uno</p>
                            <input
                                type="file"
                                accept="application/pdf"
                                onChange={(e) => handleFileChange(e.target.files[0])}
                            />
                        </label>
                        {formData.rfcPdfName && (
                            <p style={{ color: 'green' }}>Archivo cargado: {formData.rfcPdfName}</p>
                        )}


                    </div>
                    <p>O</p>
                    <label className={style.rfcText}>
                        Ingresar tu RFC:
                        <input
                            type="text"
                            name="rfc"
                            value={formData.rfc}
                            onChange={handleInputChange}
                        />
                    </label>
                </div>

                <div className={style.foliosContainer}>
                    <div className={style.Titulos}>
                        <h3>Ingresa el número de ticket</h3>
                        <h3 className={style.Titulito}>Selecciona la empresa correspondiente al ticket</h3>
                    </div>
                    {formData.tickets.map((folio) => (
                        <div key={folio.id} className={style.folioitem}>
                            <Folio folio={folio} onInputChange={actualizarFolio} />
                            <button onClick={() => eliminarFolio(folio.id)} className={style.Basura}>&#128465;</button>
                        </div>
                    ))}
                </div>
                <div className={style.botonera}>
                    <button onClick={agregarFolio}>Agregar Folio</button>
                    <button onClick={enviarFormulario}>Enviar Formulario</button>
                </div>
            </div>
        </div>
    );
};

export default Iniciales;
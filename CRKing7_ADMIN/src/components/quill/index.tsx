import React, { Component } from 'react';
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import ImageResize from 'quill-image-resize-module-react';

import './styles.css';

interface EditorProps {
  placeholder: string;
}

interface EditorState {
  editorHtml: string;
}

Quill.register('modules/imageResize', ImageResize);

/*
 * Simple editor component that takes placeholder text as a prop
 */
class Editor extends Component<EditorProps, EditorState> {
  constructor(props: EditorProps) {
    super(props);
    this.state = { editorHtml: '' };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(html: string) {
    this.setState({ editorHtml: html });
  }

  render() {
    return (
      <ReactQuill
        theme="snow"
        onChange={this.handleChange}
        value={this.state.editorHtml}
        modules={Editor.modules}
        formats={Editor.formats}
        bounds="#root"
        placeholder={this.props.placeholder}
      />
    );
  }
}

/*
 * Quill modules to attach to editor
 * See https://quilljs.com/docs/modules/ for complete options
 */
Editor.modules = {
  toolbar: [
    [{ header: '1' }, { header: '2' }, { font: [] }],
    [{ size: [] }],
    ['bold', 'italic', 'underline', 'strike', 'blockquote'],
    [{ list: 'ordered' }, { list: 'bullet' }, { indent: '-1' }, { indent: '+1' }],
    ['link', 'image', 'video'],
    ['clean'],
  ],
  clipboard: {
    // toggle to add extra line breaks when pasting HTML:
    matchVisual: false,
  },
  imageResize: {
    parchment: Quill.import('parchment'),
    modules: ['Resize', 'DisplaySize'],
  },
};

/*
 * Quill editor formats
 * See https://quilljs.com/docs/formats/
 */
Editor.formats = [
  'header',
  'font',
  'size',
  'bold',
  'italic',
  'underline',
  'strike',
  'blockquote',
  'list',
  'bullet',
  'indent',
  'link',
  'image',
  'video',
];

export default Editor;

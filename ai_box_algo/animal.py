import argparse
import os
import json
from PIL import Image

if __name__ == '__main__':
    print('>>>>>>>>>>解析命令行参数')

    # 获取命令行参数
    parser = argparse.ArgumentParser()
    # 文件路径
    parser.add_argument('-path')
    # 参数文件名
    parser.add_argument('-params', default='params.json')
    # 结果文件名
    parser.add_argument('-results', default='results.json')
    args = parser.parse_args()

    # 服务器参数的路径
    params_path = os.path.join(args.path, args.params)

    # 读取服务器参数
    print('>>>>>>>>>>读取服务器参数')
    with open(params_path, 'r', encoding='utf-8') as rf:
        params = json.load(rf)
        # 图片路径
        img_path = params['image']

        # TODO 读取图片数据，执行AI识别算法
        # print('image path: ' + img_path)
        print('>>>>>>>>>>执行AI算法')

        # 将识别结果返回给服务器（写入文件）
        result_path = os.path.join(args.path, args.results)
        img_info = Image.open(img_path)
        results = [{
            'image': params['image'],
            'width': img_info.width,
            'height': img_info.height,
            'format': img_info.format
        }, {
            'name': '牧羊犬',
            'score': .66
        }, {
            'name': '哈士奇',
            'score': .24
        }, {
            'name': '中华田园犬',
            'score': .1
        }]
        print('>>>>>>>>>>返回执行结果给服务器')
        with open(result_path, 'w', encoding='utf-8') as wf:
            json.dump(results, wf, ensure_ascii=False)

# python animal.py -path D:\webstorm_project\test -params params.json -results results.json

